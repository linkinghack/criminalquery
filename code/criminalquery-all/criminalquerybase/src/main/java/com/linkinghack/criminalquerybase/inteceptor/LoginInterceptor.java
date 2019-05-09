package com.linkinghack.criminalquerybase.inteceptor;

import com.linkinghack.criminalquerymodel.data_model.User;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@ConfigurationProperties("sso")
public class LoginInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate;
    @Setter
    private String baseUrl;

    @Autowired
    public LoginInterceptor(RestTemplate template) {
        this.restTemplate = template;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Token");
        if (request.getMethod().equals("OPTIONS")) // CORS preFlight request
            return true;
        if (token == null) {
            logger.info("Request rejected {}, methos:{}", request.getRequestURL(), request.getMethod());
            response.setStatus(405);
            return false;
        }

        // 向SSO单点登录系统询问Token是否有效
        User user = restTemplate.getForObject("https://api.tyut.life/sso/auth/{token}", User.class, token);
        logger.info("User from sso {}", user);
        if (user == null) {
            logger.info("API request rejected because of unknown token: token={}", token);
            response.setStatus(405);
            return false;
        }

        request.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        return;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        return;
    }
}
