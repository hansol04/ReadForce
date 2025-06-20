package com.readforce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readforce.dto.LiteratureDto.GetLiteratureParagraph;
import com.readforce.entity.LiteratureParagraph;
import com.readforce.enums.MessageCode;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.LiteratureParagraphRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiteratureService {
	
	private final LiteratureParagraphRepository literature_paragraph_repository;
	
	// LiteratureParagraph -> GetLiteratureParagraph 변환
	private GetLiteratureParagraph transformEntity(LiteratureParagraph literature_paragraph) {
		
		GetLiteratureParagraph get_literature_paragraph = new GetLiteratureParagraph();
		get_literature_paragraph.setCategory(literature_paragraph.getCategory());
		get_literature_paragraph.setContent(literature_paragraph.getContent());
		get_literature_paragraph.setLevel(literature_paragraph.getLevel());
		get_literature_paragraph.setLiterature_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_no());
		get_literature_paragraph.setLiterature_paragraph_no(literature_paragraph.getLiterature_paragraph_id().getLiterature_paragraph_no());
		
		return get_literature_paragraph;
		
	}

	// 타입에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByType(String type, String order_by) {
		
		List<LiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
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
		
		List<GetLiteratureParagraph> get_literature_paragraph_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			get_literature_paragraph_list.add(transformEntity(literature_paragraph));
			
		}
		
		return get_literature_paragraph_list;
		
	}

	// 타입과 난이도에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByTypeAndLevel(
			String type,
			String level,
			String order_by
	) {
		
		List<LiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
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
		
		List<GetLiteratureParagraph> get_literature_paragraph_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			get_literature_paragraph_list.add(transformEntity(literature_paragraph));
			
		}
		
		return get_literature_paragraph_list;
		
	}

	// 타입과 난이도, 카테고리에 해당하는 문학 문단 리스트 가져오기(내림차순/오름차순)
	@Transactional(readOnly = true)
	public List<GetLiteratureParagraph> getLiteratureParagraphListByTypeAndLevelAndCategory(
			String type, 
			String level,
			String category, 
			String order_by
	) {
		
		List<LiteratureParagraph> literature_paragraph_list = new ArrayList<>();
		
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
		
		List<GetLiteratureParagraph> get_literature_paragraph_list = new ArrayList<>();
		
		for(LiteratureParagraph literature_paragraph : literature_paragraph_list) {
			
			get_literature_paragraph_list.add(transformEntity(literature_paragraph));
			
		}
		
		return get_literature_paragraph_list;
		
	}
	
	

}
