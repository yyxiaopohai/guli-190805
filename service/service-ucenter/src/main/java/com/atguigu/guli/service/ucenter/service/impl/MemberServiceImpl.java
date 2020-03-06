package com.atguigu.guli.service.ucenter.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.common.base.util.MD5;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginInfoVo;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.formula.Formula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-03-03
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.countRegisterByDay(day);
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //校验参数
        //校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(nickname)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        //从redis中取出验证码，和注册对象传入的验证码比较
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(mobileCode)){
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        //校验是否注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        //注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("\n" +
                "https://guli190805.oss-cn-beijing.aliyuncs.com/avator/2020/02/19/376dd38b-1efa-4974-acd3-bb1da4ec19d4.jpg");
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || !FormUtils.isMobile(mobile)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        //使用手机号查询，没有数据就提示错误
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }

        //从member中取出密码是加密的，所以使用md5把输入的密码先加密再进行比较
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //校验用户是否被禁用
        if (member.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //生成jwtToken
        Map<String,Object> claims = new HashMap<>();

        claims.put("id",member.getId());
        claims.put("nickname",member.getNickname());
        claims.put("avatar",member.getAvatar());

        String jwtToken = JwtUtils.generateJWT(claims);
        return jwtToken;
    }

    @Override
    public LoginInfoVo getLoginInfoByJwtToken(String token) {

        Claims claims = JwtUtils.parseJWT(token);
        String id = (String) claims.get("id");
        String avatar = (String) claims.get("avatar");
        String nickname = (String) claims.get("nickname");


        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setId(id);
        loginInfoVo.setAvatar(avatar);
        loginInfoVo.setNickname(nickname);

        return loginInfoVo;
    }
}
