package com.example.classroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.classroom.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ReservationMapper extends BaseMapper<Reservation> {
  int countConflictForUpdate(@Param("classroomId") Long classroomId,
                             @Param("reservationDate") LocalDate reservationDate,
                             @Param("slotStart") Integer slotStart,
                             @Param("slotEnd") Integer slotEnd);

  List<Map<String, Object>> selectBoard(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("classroomId") Long classroomId);

  List<Map<String, Object>> selectMine(@Param("userId") Long userId);

  List<Map<String, Object>> selectPending();
}
