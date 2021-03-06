package com.atguigu.guli.service.statistics.client;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.client.exception.UcenterClientExceptionHandler;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "guli-ucenter",fallback = UcenterClientExceptionHandler.class)
public interface UcenterClient {

    @GetMapping("/admin/ucenter/member/count-register/{day}")
    public R countRegisterByDay(@PathVariable("day") String day);
}
