package com.example.classroom.service;

import com.example.classroom.entity.SysLog;
import com.example.classroom.mapper.SysLogMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncLogService {
  private final SysLogMapper sysLogMapper;

  public AsyncLogService(SysLogMapper sysLogMapper) {
    this.sysLogMapper = sysLogMapper;
  }

  @Async
  public void save(SysLog log) {
    sysLogMapper.insert(log);
  }
}
