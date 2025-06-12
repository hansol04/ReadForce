package com.readforce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.entity.Attendance;
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Status;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.AttendanceRepository;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendance_repository;
	private final MemberRepository member_repository;
	
	@Transactional
	public void recordAttendance(String email) {
		
		// 오늘의 시작과 끝 시간 설정
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
		
		// 오늘 이미 출석했는지 확인
		boolean already_attended =
				attendance_repository.existsByMemberEmailAndCreatedDateBetween(email, startOfDay, endOfDay);
		
		if(!already_attended) {
			
			Member member = member_repository.findByEmailAndStatus(email, Status.ACTIVE)
					.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
			
			Attendance attendance = new Attendance();
			attendance.setMember(member);
			attendance.setEmail(email);
			attendance.setAttendance_date(LocalDate.now());
			
			attendance_repository.save(attendance);
			
		}
		
		
	}
	
	
}
