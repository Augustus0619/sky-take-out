package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截微信小程序用户端发来的请求，校验jwt令牌

@Slf4j
@Component
public class JwtTokenUserInterceptor implements HandlerInterceptor
{

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1.从请求头中获取jwt令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2.进行jwt校验
        try
        {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            System.out.println("userid" + claims.get(JwtClaimsConstant.USER_ID).toString());
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户的id：{}", userId);
            BaseContext.setCurrentId(userId);

            //3、通过，放行
            return true;
        }
        catch (Exception e)
        {
            response.setStatus(401);
            return false;
        }
    }
}
