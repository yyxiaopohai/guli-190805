package com.atguigu.guli.service.vod.controller;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.POST;
import java.io.IOException;
import java.io.InputStream;

@Api("阿里云视频点播")
@CrossOrigin
@RestController
@RequestMapping("admin/vod/video")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("upload")
    public R uploadVideo(
            @ApiParam(name = "file",value = "文件",required = true)
            @RequestParam("file")MultipartFile file){

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = fileService.uploadVideo(inputStream,originalFilename);
            return R.ok().message("视频上传成功").data("videoId",videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }
}
