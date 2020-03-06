package com.atguigu.guli.service.statistics.client.exception;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.client.UcenterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

@Component
@Slf4j
public class UcenterClientExceptionHandler implements UcenterClient {
    @Override
    public R countRegisterByDay(String day) {
        //错误日志
        log.error("熔断器执行");
        return R.ok().data("registerCount",0);
    }
}
