package com.atguigu.guli.service.ucenter.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.MembershipKey;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-03-03
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/ucenter/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("今日注册数")
    @GetMapping("count-register/{day}")
    public R countRegisterByDay(
            @ApiParam(name = "day",value = "统计时间")
            @PathVariable String day
    ){
        Integer count = memberService.countRegisterByDay(day);
        return R.ok().data("registerCount",count);
    }

}

