package com.readforce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.MemberDto.UpdatePoint;
import com.readforce.entity.Point;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.PointRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {
	
	private final PointRepository point_repository;
		
	// 점수 수정
	@Transactional
	public void updatePoint(String email, @Valid UpdatePoint update_point) {
		
		// 포인트 조회
		Point point = point_repository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND));
				
		
		// 총 점수 수정
		if(update_point.getTotal() != null) {
			
			point.setTotal(update_point.getTotal());
			
		}
		
		// 한국어 뉴스 점수 수정
		if(update_point.getKorean_news() != null) {
			
			point.setKorean_news(update_point.getKorean_news());
			
		}
		
		// 영어 뉴스 점수 수정
		if(update_point.getEnglish_news() != null) {
			
			point.setEnglish_news(update_point.getEnglish_news());
			
		}
		
		// 일본어 뉴스 점수 수정
		if(update_point.getJapanese_news() != null) {
			
			point.setJapanese_news(update_point.getJapanese_news());
			
		}
		
		// 소설 점수 수정
		if(update_point.getNovel() != null) {
		
			point.setNovel(update_point.getNovel());
			
		}
		
		// 동화 점수 수정
		if(update_point.getFairytale() != null) {
			
			point.setFairytale(update_point.getFairytale());
			
		}
		
	}

}
