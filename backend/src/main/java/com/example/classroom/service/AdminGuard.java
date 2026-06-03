package com.example.classroom.service;

import com.example.classroom.common.BizException;
import com.example.classroom.security.CurrentUser;
import com.example.classroom.security.UserContext;
import org.springframework.stereotype.Component;

@Component
public class AdminGuard {
  public void requireAdmin() {
    CurrentUser user = UserContext.get();
    if (user == null || !user.isAdmin()) {
      throw new BizException(403, "当前账号无管理员权限");
    }
  }
}
