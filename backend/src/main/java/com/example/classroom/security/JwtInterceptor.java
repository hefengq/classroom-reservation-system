package com.example.classroom.security;

import com.example.classroom.common.BizException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
  private final JwtService jwtService;

  public JwtInterceptor(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      throw new BizException(401, "未认证，请先登录");
    }
    UserContext.set(jwtService.parse(header.substring(7)));
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    UserContext.clear();
  }
}
