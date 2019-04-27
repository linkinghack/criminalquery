package com.linkinghack.criminalquery.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogsInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logRequest(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logResponse(response);
    }

    private void logRequest(HttpServletRequest request) throws IOException {
        log.info("===========================request begin================================================");
        log.info("URI         : {}", request.getContextPath());
        log.info("Method      : {}", request.getMethod() );
        log.info("RemoteIP     : {}", request.getHeader("X-Real-IP"));
        log.info("Remote:      :{}", request.getRemoteHost());
        log.info("==========================request end================================================");

    }

    private void logResponse(HttpServletResponse response) throws IOException {
        log.info("============================response begin==========================================");
        log.info("Status code  : {}", response.getStatus());
        log.info("ContentType  : {}", response.getContentType());
        log.info("=======================response end=================================================");
    }
}
