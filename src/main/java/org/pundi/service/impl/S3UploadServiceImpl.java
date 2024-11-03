package org.pundi.service.impl;

import cn.hutool.json.JSONArray;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.pundi.common.DatasetParam;
import org.pundi.config.AwsConfig;
import org.pundi.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ttf
 * @date 2024-08-10 12:55
 **/
@Service
@Slf4j
public class S3UploadServiceImpl implements S3UploadService {

	@Autowired
	private AwsConfig awsConfig;


	private S3AsyncClient s3AsyncClient;


	private S3Presigner s3Presigner;


	// 初始化连接S3
	@PostConstruct
	public void init() throws URISyntaxException {

		if (StringUtils.isBlank(awsConfig.getAk()) && StringUtils.isBlank(awsConfig.getSk())) {
			log.info("aws not config");
			Region region = Region.US_EAST_1;
			s3AsyncClient = S3AsyncClient.builder().credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).region(region).build();

			s3Presigner = S3Presigner.builder().credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).region(region).build();

		} else {
			log.info("aws have config");
			AwsBasicCredentials creds = AwsBasicCredentials.create(awsConfig.getAk(), awsConfig.getSk());
			s3AsyncClient = S3AsyncClient.builder().serviceConfiguration(b -> b.checksumValidationEnabled(false))
				.region(Region.AWS_GLOBAL)
				.credentialsProvider(StaticCredentialsProvider.create(creds))
				.endpointOverride(new URI(awsConfig.getS3Url()))
				.build();

			s3Presigner = S3Presigner.builder()
				.region(Region.AWS_GLOBAL)
				.credentialsProvider(StaticCredentialsProvider.create(creds))
				.endpointOverride(new URI(awsConfig.getS3Url()))
				.build();
		}


	}


	private static final ThreadPoolExecutor S3_EXECUTOR_UPLOAD = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500), new ThreadFactory() {
		private final ThreadGroup threadGroup = new ThreadGroup("upload-");

		@Override
		public Thread newThread(@NonNull Runnable r) {
			return new Thread(threadGroup, r, threadGroup.getName() + "-" + r.hashCode());
		}
	}, new ThreadPoolExecutor.CallerRunsPolicy());

	private static final ThreadPoolExecutor S3_EXECUTOR_DOWNLOAD = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new ThreadFactory() {
		private final ThreadGroup threadGroup = new ThreadGroup("download");

		@Override
		public Thread newThread(@NonNull Runnable r) {
			return new Thread(threadGroup, r, threadGroup.getName() + "-" + r.hashCode());
		}
	}, new ThreadPoolExecutor.CallerRunsPolicy());

	@Override
	public boolean uploadJSONObjectsToS3(int datasetId, String datasetName, JSONArray filesData, String fileBucketName) {

		long now = System.currentTimeMillis();
		// 记录已处理条目数量
		AtomicInteger processedCount = new AtomicInteger();
		// 使用线程安全的集合来避免并发问题
		ConcurrentLinkedQueue<DatasetParam.FileDetail> resultQueue = new ConcurrentLinkedQueue<>();

		// 开始并行处理文件上传
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
			filesData.parallelStream().map(fileData -> CompletableFuture.runAsync(() -> {
				String fileHash = UUID.randomUUID().toString().replace("-", "");
				String fileName = fileBucketName + "/" + datasetName + "/" + fileHash;

				PutObjectRequest request = PutObjectRequest.builder()
					.bucket(awsConfig.getBucketName())
					.key(fileName)
					.contentType("application/json")
					.build();

				// 异步上传并避免阻塞
				s3AsyncClient.putObject(request, AsyncRequestBody.fromString(fileData.toString()))
					.whenComplete((resp, throwable) -> {
						if (throwable != null) {
							log.error("Upload failed for file {}", fileName, throwable);
						} else {
							int currentCount = processedCount.incrementAndGet();
							log.debug("Processed {} items so far. Finished item upload", currentCount);

							DatasetParam.FileDetail dto = new DatasetParam.FileDetail();
							dto.setFileUrl(fileName);
							dto.setSingleContentHash(fileHash);
							resultQueue.add(dto);
						}
					}).join();
			}, S3_EXECUTOR_UPLOAD)).toArray(CompletableFuture[]::new)
		);

		// 等待所有文件上传完成
		allFutures.join();

		// 将结果从线程安全的集合转移到最终结果集中
		List<DatasetParam.FileDetail> result = new ArrayList<>(resultQueue);


		log.info("All JSON upload finished, datasetId: [{}]", datasetId);
		log.debug("upload finish spend:[{}]ms", System.currentTimeMillis() - now);

		return true;

	}

	@Override
	public Map<String, String> downLoadJsonFromS3(List<String> objectKeys) {
		Map<String, String> result = new ConcurrentHashMap<>();

		CompletableFuture.allOf(objectKeys.stream()
			.map(objectKey -> CompletableFuture.runAsync(() -> {

				GetObjectRequest request = GetObjectRequest.builder()
					.bucket(awsConfig.getBucketName())
					.key(objectKey)
					.build();

				try {
					ResponseBytes<GetObjectResponse> response = s3AsyncClient.getObject(request, AsyncResponseTransformer.toBytes()).join();
					byte[] bytes = response.asByteArray();
					String jsonResult = new String(bytes);
					result.put(objectKey, jsonResult);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, S3_EXECUTOR_DOWNLOAD)).toArray(CompletableFuture[]::new)).join();


		return result;
	}

	@Override
	public String uploadAnnotationResultToS3(String taskId, String jsonContent, String fileBucketName, String address, String singleContentHash) {

		String fileName = fileBucketName + "/" + taskId + "/" + address + "/" + singleContentHash;
		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(awsConfig.getBucketName())
			.key(fileName)
			.contentType("application/json")
			.build();

		final PutObjectResponse response = s3AsyncClient.putObject(request, AsyncRequestBody.fromString(jsonContent)).join();
		// 检查上传是否成功
		if (response != null && response.sdkHttpResponse().isSuccessful()) {
			log.info("upload annotation result is success,fileName:{}",fileName);
			return fileName;
		} else {
			log.error("upload annotation result is fail,fileName:{}",fileName);
			return "";
		}

	}

	@Override
	public Map<String, String> getPreSignUrl(List<String> objectKeys) {

		Map<String, String> result = new ConcurrentHashMap<>();

		CompletableFuture<?>[] futures = objectKeys.stream()
			.map(objectKey -> CompletableFuture.supplyAsync(() -> {
				try {
					GetObjectRequest getObjectRequest = GetObjectRequest.builder()
						.bucket(awsConfig.getBucketName())
						.key(objectKey)
						.build();

					PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
						.signatureDuration(Duration.ofMinutes(awsConfig.getPreUrlDuration()))
						.getObjectRequest(getObjectRequest)
						.build()
					);

					String url = presignedGetObjectRequest.url().toString();
					result.put(objectKey, url);
				} catch (Exception e) {
					// 处理异常情况
					e.printStackTrace();
				}
				return null;
			}, S3_EXECUTOR_DOWNLOAD))
			.toArray(CompletableFuture[]::new);

		CompletableFuture.allOf(futures).join();

		s3Presigner.close();

		return result;
	}

	@Override
	public CompletableFuture<List<String>> listFilesInDirectory(String directoryPath) {
		ListObjectsV2Request request = ListObjectsV2Request.builder()
			.bucket(awsConfig.getBucketName())
			.prefix(directoryPath + "/")
			.build();

		return s3AsyncClient.listObjectsV2(request)
			.thenApply(response -> response.contents().stream()
				.map(S3Object::key)
				.collect(Collectors.toList())
			);
	}


}
