package com.readforce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.MemberDto.UpdatePoint;
import com.readforce.dto.PointDto.SaveChallengePoint;
import com.readforce.entity.Point;
import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
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

	// 포인트 업데이트
	@Transactional
	public void updateChallengePoint(String email, SaveChallengePoint save_challenge_point) {
		
		Point point = point_repository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND));
		
		// 뉴스
		if(save_challenge_point.getClassification().equals(Classification.NEWS.toString())) {
			
			// 영어
			if(save_challenge_point.getLanguage().equals(NewsRelate.Language.ENGLISH.toString())) {
				
				point.setEnglish_news(save_challenge_point.getPoint());
				
			}
			
			// 일본어
			if(save_challenge_point.getLanguage().equals(NewsRelate.Language.JAPANESE.toString())) {
				
				point.setJapanese_news(save_challenge_point.getPoint());
				
			}
			
			// 한국어
			if(save_challenge_point.getLanguage().equals(NewsRelate.Language.KOREAN.toString())) {
				
				point.setKorean_news(save_challenge_point.getPoint());
				
			}
			
		}
		
		// 문학
		if(save_challenge_point.getClassification().equals(Classification.LITERATURE.toString())){
			
			// 소설
			if(save_challenge_point.getType().equals(LiteratureRelate.type.NOVEL.toString())) {
				
				point.setNovel(save_challenge_point.getPoint());
				
			}
			
			// 동화
			if(save_challenge_point.getType().equals(LiteratureRelate.type.FAIRYTALE.toString())) {
				
				point.setFairytale(save_challenge_point.getPoint());
				
			}
			
		}

	}
	
	// 포인트 주간 초기화
	

}
