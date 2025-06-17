package com.readforce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readforce.dto.MemberDto.GetMemberObject;
import com.readforce.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final MemberService member_service;

	// 전체 회원 목록 조회
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-member-list")
	public ResponseEntity<List<GetMemberObject>> getAllMemberList(){
		
		List<GetMemberObject> member_list = member_service.getAllMemberList();
		
		return ResponseEntity.status(HttpStatus.OK).body(member_list);
		
	}
	
	// 
	
}
