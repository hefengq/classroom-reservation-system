package com.example.classroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.classroom.common.ApiResponse;
import com.example.classroom.common.Log;
import com.example.classroom.entity.Classroom;
import com.example.classroom.entity.Reservation;
import com.example.classroom.mapper.ClassroomMapper;
import com.example.classroom.mapper.ReservationMapper;
import com.example.classroom.service.AdminGuard;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {
  private final ClassroomMapper classroomMapper;
  private final ReservationMapper reservationMapper;
  private final AdminGuard adminGuard;

  public ClassroomController(ClassroomMapper classroomMapper, ReservationMapper reservationMapper, AdminGuard adminGuard) {
    this.classroomMapper = classroomMapper;
    this.reservationMapper = reservationMapper;
    this.adminGuard = adminGuard;
  }

  @GetMapping
  public ApiResponse<List<Classroom>> list() {
    return ApiResponse.ok(classroomMapper.selectList(new LambdaQueryWrapper<Classroom>().orderByAsc(Classroom::getRoomNumber)));
  }

  @Log("新增教室")
  @PostMapping
  public ApiResponse<Classroom> create(@RequestBody Classroom classroom) {
    adminGuard.requireAdmin();
    classroomMapper.insert(classroom);
    return ApiResponse.ok(classroom);
  }

  @Log("修改教室")
  @PutMapping("/{id}")
  public ApiResponse<Classroom> update(@PathVariable("id") Long id, @RequestBody Classroom classroom) {
    adminGuard.requireAdmin();
    classroom.setId(id);
    classroomMapper.updateById(classroom);
    return ApiResponse.ok(classroom);
  }

  @Log("删除教室")
  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable("id") Long id) {
    adminGuard.requireAdmin();
    Long reservationCount = reservationMapper.selectCount(new LambdaQueryWrapper<Reservation>().eq(Reservation::getClassroomId, id));
    if (reservationCount > 0) {
      Classroom classroom = new Classroom();
      classroom.setId(id);
      classroom.setStatus(0);
      classroomMapper.updateById(classroom);
    } else {
      classroomMapper.deleteById(id);
    }
    return ApiResponse.ok(null);
  }
}
