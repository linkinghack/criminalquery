package com.linkinghack.criminalquery.interceptor;
import com.linkinghack.criminalquery.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(403);
            logger.info("@[LoginInterceptor] API request refused. Path:{}, remoteIP:{}", request.getContextPath(), request.getHeader("X-Real-IP"));
            return false;
        }
        logger.info("@[LoginInterceptor] API request pass. Path:{}, RemoteIP:{}", request.getContextPath(), request.getHeader("X-Real-IP"));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
