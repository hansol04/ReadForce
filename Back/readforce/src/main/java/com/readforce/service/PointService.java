package com.readforce.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.PointDto.SaveChallengePoint;
import com.readforce.entity.Point;
import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.PointRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointService {
	
	private final PointRepository point_repository;

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
	@Transactional
	@Scheduled(cron = "0 0 0 * * SUN")
	public void resetWeeklyPoint() {
		
		log.info("모든 사용자의 포인트를 초기화 합니다.");
		point_repository.resetAllPoint();
		log.info("주간 포인트 초기화 완료");
		
	}

}
