package com.example.classroom.service;

import com.example.classroom.common.BizException;
import com.example.classroom.dto.AuditRequest;
import com.example.classroom.dto.ReservationApplyRequest;
import com.example.classroom.entity.Reservation;
import com.example.classroom.mapper.ReservationMapper;
import com.example.classroom.security.CurrentUser;
import com.example.classroom.security.UserContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
  private final ReservationMapper reservationMapper;
  private final NotificationService notificationService;

  public ReservationService(ReservationMapper reservationMapper, NotificationService notificationService) {
    this.reservationMapper = reservationMapper;
    this.notificationService = notificationService;
  }

  public List<Map<String, Object>> board(LocalDate startDate, LocalDate endDate, Long classroomId) {
    return reservationMapper.selectBoard(startDate, endDate, classroomId);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Reservation apply(ReservationApplyRequest request) {
    CurrentUser user = UserContext.get();
    if (request.getSlotStart() == null || request.getSlotEnd() == null || request.getSlotStart() > request.getSlotEnd()) {
      throw new BizException(500, "预约节次不合法");
    }
    int conflict = reservationMapper.countConflictForUpdate(
        request.getClassroomId(),
        request.getReservationDate(),
        request.getSlotStart(),
        request.getSlotEnd());
    if (conflict > 0) {
      throw new BizException(500, "该时段已被预约或正在审批中，请刷新看板");
    }
    Reservation reservation = new Reservation();
    reservation.setUserId(user.getId());
    reservation.setClassroomId(request.getClassroomId());
    reservation.setReservationDate(request.getReservationDate());
    reservation.setSlotStart(request.getSlotStart());
    reservation.setSlotEnd(request.getSlotEnd());
    reservation.setPurpose(request.getPurpose());
    reservation.setStatus(0);
    reservationMapper.insert(reservation);
    return reservation;
  }

  public List<Map<String, Object>> myReservations() {
    return reservationMapper.selectMine(UserContext.get().getId());
  }

  public List<Map<String, Object>> pending() {
    return reservationMapper.selectPending();
  }

  @Transactional
  public Reservation audit(Long id, AuditRequest request) {
    if (request.getStatus() == null || (request.getStatus() != 1 && request.getStatus() != 2)) {
      throw new BizException(500, "审批状态必须为通过或驳回");
    }
    Reservation reservation = reservationMapper.selectById(id);
    if (reservation == null || reservation.getStatus() != 0) {
      throw new BizException(500, "预约单不存在或已审批");
    }
    reservation.setStatus(request.getStatus());
    reservation.setRejectReason(request.getStatus() == 2 ? request.getRejectReason() : null);
    reservationMapper.updateById(reservation);
    notificationService.sendAuditMessage(reservation);
    return reservation;
  }
}
