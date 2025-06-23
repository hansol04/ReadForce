package com.readforce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.readforce.dto.PointDto.PointRanking;
import com.readforce.entity.Member;
import com.readforce.entity.Point;
import com.readforce.enums.Classification;
import com.readforce.enums.LiteratureRelate;
import com.readforce.enums.MessageCode;
import com.readforce.enums.NewsRelate;
import com.readforce.exception.RankingException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;
import com.readforce.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {
	
	private final MemberRepository member_repository;
	private final PointRepository point_repository;

	// 랭킹 반환
	public List<?> getRankingByClassificationAndTypeOrLanguage(
			String classification,
			String type, 
			String language
	) {
		
		// 뉴스 영어 탑 50명 회원 반환
		if(classification.equals(Classification.NEWS.toString()) && language.equals(NewsRelate.Language.ENGLISH.toString())) {
			
			List<Point> point_list = point_repository.findAllWithEnglishNews();
			
			if(point_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND);
				
			}
			
			List<PointRanking> point_ranking_list = new ArrayList<>(); 
			
			for(Point point : point_list) {
				
				Member member = member_repository.findByEmail(point.getEmail())
						.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
				
				PointRanking point_ranking = new PointRanking();
				point_ranking.setEnglish_news(point.getEnglish_news());
				point_ranking.setNickname(member.getNickname());
				
				point_ranking_list.add(point_ranking);
				
			}
			
			return point_ranking_list;
			
		}
		
		// 뉴스 일본어 탑 50명 회원 반환
		if(classification.equals(Classification.NEWS.toString()) && language.equals(NewsRelate.Language.JAPANESE.toString())) {
			
			List<Point> point_list = point_repository.findAllWithJapaneseNews();
			
			if(point_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND);
				
			}
			
			List<PointRanking> point_ranking_list = new ArrayList<>(); 
			
			for(Point point : point_list) {
				
				Member member = member_repository.findByEmail(point.getEmail())
						.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
				
				PointRanking point_ranking = new PointRanking();
				point_ranking.setJapanese_news(point.getJapanese_news());
				point_ranking.setNickname(member.getNickname());
				
				point_ranking_list.add(point_ranking);
				
			}
			
			return point_ranking_list;
			
		}
		
		
		// 뉴스 한국어 탑 50명 회원 반환
		if(classification.equals(Classification.NEWS.toString()) && language.equals(NewsRelate.Language.KOREAN.toString())) {
			
			List<Point> point_list = point_repository.findAllWithKoreanNews();
			
			if(point_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND);
				
			}
			
			List<PointRanking> point_ranking_list = new ArrayList<>(); 
			
			for(Point point : point_list) {
				
				Member member = member_repository.findByEmail(point.getEmail())
						.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
				
				PointRanking point_ranking = new PointRanking();
				point_ranking.setKorean_news(point.getKorean_news());
				point_ranking.setNickname(member.getNickname());
				
				point_ranking_list.add(point_ranking);
				
			}
			
			return point_ranking_list;
			
			
		}
		
		// 문학 소설 탑 50명 회원 반환
		if(classification.equals(Classification.LITERATURE.toString()) && language.equals(LiteratureRelate.type.NOVEL.toString())) {
			
			List<Point> point_list = point_repository.findAllWithNovel();
			
			if(point_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND);
				
			}
			
			List<PointRanking> point_ranking_list = new ArrayList<>(); 
			
			for(Point point : point_list) {
				
				Member member = member_repository.findByEmail(point.getEmail())
						.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
				
				PointRanking point_ranking = new PointRanking();
				point_ranking.setNovel(point.getNovel());
				point_ranking.setNickname(member.getNickname());
				
				point_ranking_list.add(point_ranking);
				
			}
			
			return point_ranking_list;
			
		}
		
		// 문학 동화 탑 50명 회원 반환
		if(classification.equals(Classification.LITERATURE.toString()) && language.equals(LiteratureRelate.type.FAIRYTALE.toString())) {
			
			List<Point> point_list = point_repository.findAllWithFairytale();
			
			if(point_list.isEmpty()) {
				
				throw new ResourceNotFoundException(MessageCode.POINT_NOT_FOUND);
				
			}
			
			List<PointRanking> point_ranking_list = new ArrayList<>(); 
			
			for(Point point : point_list) {
				
				Member member = member_repository.findByEmail(point.getEmail())
						.orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND));
				
				PointRanking point_ranking = new PointRanking();
				point_ranking.setFairytale(point.getFairytale());
				point_ranking.setNickname(member.getNickname());
				
				point_ranking_list.add(point_ranking);
				
			}
			
			return point_ranking_list;			
		}
		

		throw new RankingException(MessageCode.GET_RANKING_LIST_FAIL);
		
	}


	
}
