package com.atguigu.guli.service.edu.client.exception;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.client.OssClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OssClientExceptionHandler implements OssClient {
    @Override
    public R removeFile(String url) {
        log.error("熔断器执行");
        return R.ok().data("cover","");
    }
}
