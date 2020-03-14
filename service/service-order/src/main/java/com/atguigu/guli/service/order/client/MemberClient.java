package com.atguigu.guli.service.order.client;

import com.atguigu.guli.service.base.dto.MemberDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("guli-ucenter")
public interface MemberClient {

    @GetMapping(value = "/api/ucenter/member/inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(@PathVariable("memberId") String memberId);
}
