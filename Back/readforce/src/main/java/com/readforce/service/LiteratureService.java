package com.readforce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
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
		
		List<GetLiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
			//	literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeOrderByAsc(type);
		
			case "DESC":
			//	literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeOrderByDesc(type);
				
		}
		 
				
		
		
		
		return null;
	}

}
