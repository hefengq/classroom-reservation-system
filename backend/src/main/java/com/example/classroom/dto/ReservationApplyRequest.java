package com.example.classroom.dto;

import java.time.LocalDate;

public class ReservationApplyRequest {
  private Long classroomId;
  private LocalDate reservationDate;
  private Integer slotStart;
  private Integer slotEnd;
  private String purpose;

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
}
