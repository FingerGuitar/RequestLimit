package com.finger.limit.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finger.limit.annotation.RequestLimit;
import com.finger.limit.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
 
/**
 * create by: lixh
 * description: 限流拦截器
 * create time: 2021/8/4 9:36
 * @return
 */
 
@Component
public class RequestLimitInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
 
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                // 获取RequestLimit注解
                RequestLimit requestLimit = handlerMethod.getMethodAnnotation(RequestLimit.class);
                if (null==requestLimit) {
                    return true;
                }
                //限制的时间范围
                int seconds = requestLimit.second();
                //时间内的 最大次数
                int maxCount = requestLimit.maxCount();
                String ipAddr = IpUtil.getIpAddr(request);
                // 存储key
                String key =  ipAddr+":"+request.getContextPath() + ":" + request.getServletPath();
                // 已经访问的次数
                Integer count = (Integer) redisTemplate.opsForValue().get(key);
                log.info("检测到目前ip对接口={}已经访问的次数{}", request.getServletPath() , count);
                if (null == count || -1 == count) {
                    redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
                    return true;
                }
                if (count < maxCount) {
                    redisTemplate.opsForValue().increment(key);
                    return true;
                }
                log.warn("请求过于频繁请稍后再试");
                returnData(response);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.warn("请求过于频繁请稍后再试");
            e.printStackTrace();
        }
        return true;
    }
 
    public void returnData(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        //这里传提示语可以改成自己项目的返回数据封装的类
        response.getWriter().println(objectMapper.writeValueAsString("请求过于频繁请稍后再试"));
        return;
    }
 
}