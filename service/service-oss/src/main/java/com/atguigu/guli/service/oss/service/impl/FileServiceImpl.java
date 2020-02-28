package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;
    @Override
    public String upload(InputStream inputStream, String originalFilename, String module) {

        String bucketname = ossProperties.getBucketname();
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();


        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        String newFileName = UUID.randomUUID().toString();
        String fileExtention = originalFilename.substring(originalFilename.lastIndexOf("."));

        String folder = new DateTime().toString("yyyy/MM/dd");

        String yourObjectName = new StringBuffer()
                .append(module)
                .append("/")
                .append(folder)
                .append("/")
                .append(newFileName)
                .append(fileExtention)
                .toString();

        ossClient.putObject(bucketname, yourObjectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        //https://guli190805.oss-cn-beijing.aliyuncs.com/head-portrait/u%3D2844401865%2C2654823704%26fm%3D26%26gp%3D0.jpg
        return new StringBuffer()
                .append("https://")
                .append(bucketname)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(yourObjectName)
                .toString();
    }

    @Override
    public void removeFile(String url) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String bucketname = ossProperties.getBucketname();
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();



        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        //url :https://guli190805.oss-cn-beijing.aliyuncs.com/avator/2020/02/20/1b639459-5c5c-4158-b1e7-fd33512c4223.jpg
        String port = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(port.length());

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketname, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }
}
