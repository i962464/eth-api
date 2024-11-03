package org.pundi.service;

import cn.hutool.json.JSONArray;
import org.pundi.common.DatasetParam;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ttf
 * @date 2024-08-10 12:55
 **/
public interface S3UploadService {


	boolean uploadJSONObjectsToS3(int datasetId, String datasetName, JSONArray filesData, String fileBucketName);


	Map<String, String> downLoadJsonFromS3(List<String> objectKeys);


	String uploadAnnotationResultToS3(String taskId, String jsonContent, String fileBucketName, String address, String singleContentHash);

	Map<String, String> getPreSignUrl(List<String> objectKeys);

	/**
	 * 获取给定目录下的所有文件
	 * @param directoryPath
	 * @return
	 */
	CompletableFuture<List<String>> listFilesInDirectory(String directoryPath);
}
