package com.example.classroom.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.classroom.common.BizException;
import com.example.classroom.entity.User;
import com.example.classroom.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAdminService {
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public UserAdminService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public User create(User user) {
    ensureUsernameFree(user.getUsername());
    user.setRole(user.getRole() == null ? "USER" : user.getRole());
    user.setPassword(encoder.encode(user.getPassword()));
    userMapper.insert(user);
    user.setPassword(null);
    return user;
  }

  private void ensureUsernameFree(String username) {
    if (username == null || username.isBlank()) {
      throw new BizException(500, "用户名不能为空");
    }
    Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    if (count > 0) {
      throw new BizException(500, "用户名已存在: " + username);
    }
  }
}
