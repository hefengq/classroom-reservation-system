package com.example.classroom.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.classroom.common.BizException;
import com.example.classroom.entity.User;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.security.JwtService;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserMapper userMapper;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(UserMapper userMapper, JwtService jwtService) {
    this.userMapper = userMapper;
    this.jwtService = jwtService;
  }

  public Map<String, Object> login(String username, String password) {
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    if (user == null || !encoder.matches(password, user.getPassword())) {
      throw new BizException(401, "账号或密码错误");
    }
    String token = jwtService.createToken(user.getId(), user.getUsername(), user.getRole());
    return Map.of("token", token, "role", user.getRole(), "userId", user.getId(), "nickname", user.getNickname());
  }
}
