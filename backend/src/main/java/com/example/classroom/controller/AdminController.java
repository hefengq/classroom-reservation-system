package com.example.classroom.controller;

import com.example.classroom.common.ApiResponse;
import com.example.classroom.common.Log;
import com.example.classroom.dto.AuditRequest;
import com.example.classroom.entity.Reservation;
import com.example.classroom.entity.User;
import com.example.classroom.service.AdminGuard;
import com.example.classroom.service.ReservationService;
import com.example.classroom.service.UserAdminService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  private final AdminGuard adminGuard;
  private final UserAdminService userAdminService;
  private final ReservationService reservationService;

  public AdminController(AdminGuard adminGuard, UserAdminService userAdminService, ReservationService reservationService) {
    this.adminGuard = adminGuard;
    this.userAdminService = userAdminService;
    this.reservationService = reservationService;
  }

  @Log("新增用户")
  @PostMapping("/users")
  public ApiResponse<User> createUser(@RequestBody User user) {
    adminGuard.requireAdmin();
    return ApiResponse.ok(userAdminService.create(user));
  }


  @GetMapping("/reservations/pending")
  public ApiResponse<List<Map<String, Object>>> pending() {
    adminGuard.requireAdmin();
    return ApiResponse.ok(reservationService.pending());
  }

  @Log("审批预约")
  @PutMapping("/reservations/audit/{id}")
  public ApiResponse<Reservation> audit(@PathVariable("id") Long id, @RequestBody AuditRequest request) {
    adminGuard.requireAdmin();
    return ApiResponse.ok(reservationService.audit(id, request));
  }
}
