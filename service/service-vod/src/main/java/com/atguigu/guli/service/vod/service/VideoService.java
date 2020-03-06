package com.atguigu.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

public interface VideoService {
    String uploadVideo(InputStream inputStream, String originalFilename);

    void removeVideo(String vodId);

    String getVideoPlayAuth(String videoSourceId) throws ClientException;

    void removeVideoByIdList(List<String> videoSourceIdList) throws ClientException;
}
