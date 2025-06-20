package com.readforce.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor	
@Validated
public class ChallengeController {

	// 도전 문제 가져오기(뉴스(영어, 일본어, 한국어), 문학(소설, 동화))

	
}
