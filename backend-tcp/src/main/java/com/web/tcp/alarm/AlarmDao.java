package com.web.tcp.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmDao extends JpaRepository<Alarm, String> {

    List<Alarm> findAlarmByMemberId(String memberId);
}
