package com.readforce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.LiteratureParagraphRepository;
import com.readforce.repository.LiteratureRepository;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiteratureService {
	
	private final LiteratureRepository literature_repository;
	private final LiteratureParagraphRepository literature_paragraph_repository;

	// 타입에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByType(String type, String order_by) {
		
		List<GetLiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeOrderByAsc(type);
				break;
				
			default:
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeOrderByDesc(type);
				
		}
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		return literature_paragraph_list;
		
	}

	// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByTypeAndLevel(
			String type,
			String level,
			String order_by
	) {
		
		List<GetLiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelOrderByAsc(type, level);
				break;
			
			default :
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelOrderByDesc(type, level);
		}
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		return literature_paragraph_list;
		
	}

	// 타입과 난이도, 카테고리에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategory(
			String type, 
			String level,
			String category, 
			String order_by
	) {
		
		List<GetLiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
		// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기
		switch(order_by) {
		
			case "ASC":
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByAsc(type, level, category);
				break;
			
			default :
				literature_paragraph_list = literature_paragraph_repository.getLiteratureParagraphListByTypeAndLevelAndCategoryOrderByDesc(type, level, category);
		
		}
		
		if(literature_paragraph_list.isEmpty()) {
			
			throw new ResourceNotFoundException(MessageCode.LITERATURE_NOT_FOUND);
			
		}
		
		return literature_paragraph_list;
		
	}
	
	

}
