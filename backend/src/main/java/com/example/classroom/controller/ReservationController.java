package com.example.classroom.controller;

import com.example.classroom.common.ApiResponse;
import com.example.classroom.common.Log;
import com.example.classroom.dto.ReservationApplyRequest;
import com.example.classroom.entity.Reservation;
import com.example.classroom.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReservationController {
  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping("/classrooms/board")
  public ApiResponse<List<Map<String, Object>>> board(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(name = "classroomId", required = false) Long classroomId) {
    return ApiResponse.ok(reservationService.board(startDate, endDate, classroomId));
  }

  @Log("提交预约申请")
  @PostMapping("/reservations/apply")
  public ApiResponse<Reservation> apply(@RequestBody ReservationApplyRequest request) {
    return ApiResponse.ok(reservationService.apply(request));
  }

  @GetMapping("/reservations/my")
  public ApiResponse<List<Map<String, Object>>> my() {
    return ApiResponse.ok(reservationService.myReservations());
  }
}
