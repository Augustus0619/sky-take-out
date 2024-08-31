package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService
{
    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 微信登录,根据用户code请求到openId,为用户生成jwt令牌,为新用户数据库内注册
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO)
    {
        String code = userLoginDTO.getCode();
        String openid = getOpenId(code);

        System.out.println("openid:" + openid);
        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid == null)
        {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //openid是微信用户的唯一标识
        User user = userMapper.getByOpenid(openid);

        //为新用户注册
        if(user == null)
        {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code
     * @return
     */
    public String getOpenId(String code)
    {
        //static String doGet(String url,Map<String,String> paramMap) HttpClient get逻辑封装到工具类该函数
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN,map);//微信接口服务返回session_key和opendId

        JSONObject jsonObject = JSON.parseObject(json);

        System.out.println("Response from WeChat: " + json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}

//1. 小程序端，调用wx.login()获取code，就是授权码。
//        2. 小程序端，调用wx.request()发送请求并携带code，请求开发者服务器(自己编写的后端服务)。
//        3. 开发者服务端，通过HttpClient向微信接口服务发送请求，并携带appId+appsecret+code三个参数。
//        4. 开发者服务端，接收微信接口服务返回的数据，session_key+opendId等。opendId是微信用户的唯一标识。
//        5. 开发者服务端，自定义登录态，生成令牌(token)和openid等数据返回给小程序端，方便后绪请求身份校验。
//        6. 小程序端，收到自定义登录态，存储storage。
//        7. 小程序端，后绪通过wx.request()发起业务请求时，携带token。
//        8. 开发者服务端，收到请求后，通过携带的token，解析当前登录用户的id。
//        9. 开发者服务端，身份校验通过后，继续相关的业务逻辑处理，最终返回业务数据。
