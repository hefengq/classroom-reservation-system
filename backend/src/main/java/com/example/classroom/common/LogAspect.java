package com.example.classroom.common;

import com.example.classroom.entity.SysLog;
import com.example.classroom.security.CurrentUser;
import com.example.classroom.security.UserContext;
import com.example.classroom.service.AsyncLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {
  private final AsyncLogService asyncLogService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public LogAspect(AsyncLogService asyncLogService) {
    this.asyncLogService = asyncLogService;
  }

  @Around("@annotation(log)")
  public Object around(ProceedingJoinPoint point, Log log) throws Throwable {
    Object result = point.proceed();
    saveLog(point, log.value());
    return result;
  }

  public void saveLog(ProceedingJoinPoint point, String operation) {
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attrs == null ? null : attrs.getRequest();
    CurrentUser user = UserContext.get();
    SysLog sysLog = new SysLog();
    sysLog.setUsername(user == null ? "anonymous" : user.getUsername());
    sysLog.setOperation(operation);
    sysLog.setMethod(point.getSignature().toShortString());
    sysLog.setIp(request == null ? "" : request.getRemoteAddr());
    try {
      sysLog.setParams(objectMapper.writeValueAsString(point.getArgs()));
    } catch (Exception ex) {
      sysLog.setParams("参数序列化失败");
    }
    sysLog.setCreateTime(LocalDateTime.now());
    asyncLogService.save(sysLog);
  }
}
