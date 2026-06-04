package com.example.classroom.websocket;

import com.example.classroom.security.CurrentUser;
import com.example.classroom.security.JwtService;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {
  private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
  private final JwtService jwtService;

  public NotificationWebSocketHandler(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    CurrentUser user = extractUser(session.getUri());
    if (user != null) {
      sessions.put(user.getId(), session);
    } else {
      session.close(CloseStatus.NOT_ACCEPTABLE.withReason("missing or invalid token"));
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.values().remove(session);
  }

  public void sendToUser(Long userId, String json) {
    WebSocketSession session = sessions.get(userId);
    if (session != null && session.isOpen()) {
      try {
        session.sendMessage(new TextMessage(json));
      } catch (IOException ignored) {
        sessions.remove(userId);
      }
    }
  }

  private CurrentUser extractUser(URI uri) {
    if (uri == null || uri.getQuery() == null) {
      return null;
    }
    for (String pair : uri.getQuery().split("&")) {
      String[] kv = pair.split("=", 2);
      if (kv.length == 2 && "token".equals(kv[0])) {
        try {
          return jwtService.parse(URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        } catch (Exception ex) {
          return null;
        }
      }
    }
    return null;
  }
}
