package io.n0sense.examsystem.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Log
public class SecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        // 检查登陆状态
        String username = (String) request.getSession().getAttribute("username");
        if (path.contains("/error") || path.contains("/index") || path.contains("/login")
                || path.contains("/register") || path.equals("/")) {
            return true;
        } else {
            if (!StringUtils.hasLength(username)) {
                response.sendRedirect("/");
                return false;
            } else {
                return true;
            }
        }
    }
}
