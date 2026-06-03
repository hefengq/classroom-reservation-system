package com.example.classroom.service;

import com.example.classroom.entity.Reservation;
import com.example.classroom.websocket.NotificationWebSocketHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
  private final NotificationWebSocketHandler notificationHandler;

  public NotificationService(NotificationWebSocketHandler notificationHandler) {
    this.notificationHandler = notificationHandler;
  }

  @Async
  public void sendAuditMessage(Reservation reservation) {
    String statusText = reservation.getStatus() == 1 ? "已通过" : "已驳回";
    String json = "{\"type\":\"AUDIT_RESULT\",\"reservationId\":" + reservation.getId()
        + ",\"message\":\"您的教室预约" + statusText + "\"}";
    notificationHandler.sendToUser(reservation.getUserId(), json);
  }
}
