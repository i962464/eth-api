package org.pundi.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ttf
 * @date 2024-08-08 10:08
 **/
public class DatasetParam {


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DatasetOutput {

        @ApiModelProperty("数据集id")
        private int datasetId;

        @ApiModelProperty("名称")
        private String name;


        @ApiModelProperty("语言")
        private List<String> language;

        @ApiModelProperty("文件大小(单位 bytes)")
        private long fileSize;

        @ApiModelProperty("用户地址")
        private String address;

        @ApiModelProperty("数据集状态: 1:上传中 2:已上传完成")
        private int status;

        @ApiModelProperty("是否mint NFT: true 已mint，false 未mint")
        private Boolean mintNft=false;

        @ApiModelProperty("若已经mint NFT 则才会有值")
        private long tokenId;

        @ApiModelProperty("NFT 合约地址")
        private String contractAddr;

        @ApiModelProperty("文件数据类型：1 文本，2图片")
        private int dataType;


        @ApiModelProperty("数据集条目数")
        private long resourceCount;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DatasetNftOutput {

        @ApiModelProperty("数据集id")
        private int datasetId;

        @ApiModelProperty("名称")
        private String name;

        @ApiModelProperty("NFT")
        private long tokenId;

        @ApiModelProperty("合约地址")
        private String contractAddr;

        @ApiModelProperty("用户地址")
        private String address;

        @ApiModelProperty("文件数据类型：1 文本，2图片")
        private int dataType;

        @ApiModelProperty("文件大小(单位 bytes)")
        private long fileSize;

        @ApiModelProperty("数据集条目数")
        private long resourceCount;


    }

//
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Data
//    public static class DatasetNftInput extends PageInput {
//
//        @ApiModelProperty("用户地址")
//        @NotBlank
//        private String address;
//    }
//
//
//    @EqualsAndHashCode(callSuper = true)
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Data
//    public static class MyDatasetInput extends PageInput {
//
//        @ApiModelProperty("用户地址")
//        @NotBlank
//        private String address;
//
//
//    }




    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class FileDetail {

        @ApiModelProperty("文件格式")
        private String singleContentHash;

        @ApiModelProperty("文件url")
        private String fileUrl;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DatesetDetailOutput {

        @ApiModelProperty("数据集id")
        private int datasetId;

        @ApiModelProperty("名称")
        private String name;

        @ApiModelProperty("数据类型: 0新闻分类 1情感分析 2命名实体识别 3词性标注 4文本摘要 ")
        private int contentType;

        @ApiModelProperty("语言")
        private List<String> language;

        @ApiModelProperty("数据类型: 1text,2image,3audio")
        private int fileType;

        @ApiModelProperty("文件大小(所有文加起来)")
        private String fileSize;

        @ApiModelProperty("用户地址")
        private String address;

        @ApiModelProperty("资源条数")
        private long resourceCount;

        @ApiModelProperty("文件格式")
        private List<String> fileFormat;

        @ApiModelProperty("描述")
        private String describe;

        @ApiModelProperty("数据集状态: 1:上传中 2:已上传完成(等待mint NFT) 3: 已mint NFT")
        private int status;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DatasetDetailInput{

        @ApiModelProperty("数据集id")
        @NotNull
        private Integer datasetId;

    }

}
