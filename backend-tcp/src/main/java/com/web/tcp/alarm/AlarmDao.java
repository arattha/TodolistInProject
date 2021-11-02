package com.web.tcp.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmDao extends JpaRepository<Alarm, String> {

    Optional<Alarm> findAlarmById(String alarmId);
    List<Alarm> findAlarmByMemberId(String memberId);

}
