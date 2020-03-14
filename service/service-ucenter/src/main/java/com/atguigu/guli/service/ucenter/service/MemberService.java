package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginInfoVo;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-03-03
 */
public interface MemberService extends IService<Member> {

    Integer countRegisterByDay(String day);

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    LoginInfoVo getLoginInfoByJwtToken(String token);

    Member getByOpenid(String openid);

    MemberDto getMemberDtoByMemberId(String memberId);
}
