package com.atguigu.guli.service.vod.service;

import java.io.InputStream;

public interface FileService {
    String uploadVideo(InputStream inputStream, String originalFilename);
}
