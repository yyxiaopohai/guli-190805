package com.atguigu.guli.service.edu.client;

import com.atguigu.guli.common.base.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient("guli-vod")
public interface VodClient {

    @DeleteMapping("admin/vod/video/remove")
    public R removeVideoByIdList(@RequestBody List<String> videoSourceIdList);
}
