package com.example.classroom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("busi_reservation")
public class Reservation {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long userId;
  private Long classroomId;
  private LocalDate reservationDate;
  private Integer slotStart;
  private Integer slotEnd;
  private String purpose;
  private Integer status;
  private String rejectReason;
  private LocalDateTime createTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getClassroomId() {
    return classroomId;
  }

  public void setClassroomId(Long classroomId) {
    this.classroomId = classroomId;
  }

  public LocalDate getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(LocalDate reservationDate) {
    this.reservationDate = reservationDate;
  }

  public Integer getSlotStart() {
    return slotStart;
  }

  public void setSlotStart(Integer slotStart) {
    this.slotStart = slotStart;
  }

  public Integer getSlotEnd() {
    return slotEnd;
  }

  public void setSlotEnd(Integer slotEnd) {
    this.slotEnd = slotEnd;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getRejectReason() {
    return rejectReason;
  }

  public void setRejectReason(String rejectReason) {
    this.rejectReason = rejectReason;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }
}
