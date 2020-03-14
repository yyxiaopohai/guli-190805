package com.atguigu.guli.service.gateway.filter;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author helen
 * @since 2020/3/7
 */
@Component
public class AuthGlobalFilter  implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //谷粒学院api接口，校验用户必须登录
        if(antPathMatcher.match("/api/**/auth/**", path)) {

            //从header中获取token值
            List<String> tokenList = request.getHeaders().get("token");
            if(null == tokenList) { //如果没有token则，跳转到自定义结果
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            } else { //如果header中有token则对token进行校验
                Boolean isCheck = JwtUtils.checkJWT(tokenList.get(0));
                if(!isCheck) { //如果校验失败，跳转到自定义结果
                    ServerHttpResponse response = exchange.getResponse();
                    return out(response);
                }
            }
        }

        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject message = new JsonObject();
        message.addProperty("success", ResultCodeEnum.LOGIN_AURH.getSuccess());
        message.addProperty("code", ResultCodeEnum.LOGIN_AURH.getCode());
        message.addProperty("data", "");
        message.addProperty("message", ResultCodeEnum.LOGIN_AURH.getMessage());
        byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
