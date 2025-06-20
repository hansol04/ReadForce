package com.readforce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
import com.readforce.entity.Literature;
import com.readforce.repository.LiteratureParagraphRepository;
import com.readforce.repository.LiteratureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiteratureService {
	
	private final LiteratureRepository literature_repository;
	private final LiteratureParagraphRepository literature_paragraph_repository;

	// 타입에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByType(String type, String order_by) {
		
		// 타입에 해당하는 문학 문단 리스트 가져오기
	//	List<GetLiteratureParagraph> literature_paragraph_list =
				
		
		
		
		return null;
	}

}
