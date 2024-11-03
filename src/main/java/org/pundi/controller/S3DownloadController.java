package org.pundi.controller;

import cn.hutool.json.JSONUtil;
import org.pundi.config.AwsConfig;
import org.pundi.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年11月01日 16:47:00
 */


@RestController
public class S3DownloadController {

	@Autowired
	private S3UploadService s3UploadService;

	@Autowired
	private AwsConfig awsConfig;


	@GetMapping("/uploadFolder")
	public String upload() {
		String resultToS3 = s3UploadService.uploadAnnotationResultToS3("11", "{\"aa\":12, \"bb\":\"bb\"}",
			awsConfig.getAnnotationFolder(), "0x0001AA", "999");
		System.out.println(resultToS3);
		return resultToS3;
	}

	/**
	 * 下载 S3 文件夹并返回压缩文件
	 *
	 * @param folderPath S3 文件夹路径
	 * @return 下载的 ZIP 文件
	 */

	@GetMapping("/downloadFolder")
	public String downloadFolderFromS3(@RequestParam String folderPath) {
		try {

			// 获取文件夹中的所有文件
			Map<String, String> files = s3UploadService.downLoadJsonFromS3(List.of(folderPath));
			return JSONUtil.toJsonStr(files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/download-csv")
	public void downloadFilesAsCsv(@RequestParam String directoryPath, HttpServletResponse response) {
		try {
			// 异步获取目录下所有文件
			CompletableFuture<List<String>> futureFileKeys = s3UploadService.listFilesInDirectory(directoryPath);
			List<String> fileKeys = futureFileKeys.join(); // 获取文件列表

			// 准备CSV文件
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"files.csv\"");

			PrintWriter writer = response.getWriter();
			writer.println("File Key,TaskID,Address,DatasetDetailID,Content");

			// 遍历每个文件，下载并写入CSV
			Map<String, String> filesContent = s3UploadService.downLoadJsonFromS3(fileKeys);
			for (Map.Entry<String, String> entry : filesContent.entrySet()) {
				String fileKey = entry.getKey();
				String fileContent = entry.getValue();

				// 按“/”分割路径，提取 TaskID, Address, DatasetDetailID
				String[] parts = fileKey.split("/");
				String taskId = parts.length > 1 ? parts[1] : "";
				String address = parts.length > 2 ? parts[2] : "";
				String datasetDetailId = parts.length > 3 ? parts[3] : "";

				// 写入 CSV，注意转义特殊字符
				writer.println(fileKey + "，" +taskId + "," + address + "," + datasetDetailId + "," + "\"" + fileContent.replace("\"", "\"\"") + "\"");

			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



