package com.web.tip.alarm;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmDao extends JpaRepository<Alarm, String> {

    Optional<Alarm> findAlarmById(String alarmId);
    List<Alarm> findAlarmByMemberId(String memberId);
    Slice<Alarm> findAlarmByMemberIdAndIsShowIsFalse(String memberId, Pageable pagable);
    long countAlarmByMemberIdAndIsShowIsFalse(String memberId);
}
