package org.pundi.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 读取公共配置：fx-annotation-common.yaml
 *
 * @author ttf
 * @date 2024-08-13 23:48
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
@Slf4j
public class AwsConfig {


	private String bucketName;

	private String s3Url;

	private String ak;

	private String sk;

	/**
	 * 标注结构上传到S3
	 */
	private String annotationFolder;

	/**
	 * 预签名地址过期时间
	 */
	private int preUrlDuration;

}