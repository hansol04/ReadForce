package com.readforce.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.readforce.dto.MemberDto;
import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Name;
import com.readforce.repository.MemberRepository;
import com.readforce.service.AttendanceService;
import com.readforce.service.AuthService;
import com.readforce.service.FileService;
import com.readforce.service.MemberService;
import com.readforce.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
	
	private final MemberService member_service;
	private final AuthService auth_service;
	private final AuthenticationManager authentication_manager;
	private final JwtUtil jwt_util;
	private final FileService file_service;
	private final AttendanceService attendance_service;
    private final MemberRepository member_repository;

	
	// e-mail 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@Valid @RequestBody MemberDto.SignIn sign_in){	
    	
    	// 사용자 인증
    	authentication_manager.authenticate(
    			new UsernamePasswordAuthenticationToken(sign_in.getEmail(), sign_in.getPassword())
    	);
    	
    	// 출석 체크
    	attendance_service.recordAttendance(sign_in.getEmail());
    	
    	// jwt 생성
    	final UserDetails user_details = auth_service.loadUserByUsername(sign_in.getEmail());
    	final String access_token = jwt_util.generateAcessToken(user_details);
    	
        // 닉네임 조회
        Member member = member_repository.findByEmail(sign_in.getEmail())
            .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
    	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(Name.ACCESS_TOKEN.toString(), access_token, "nickname", member.getNickname(), MessageCode.MESSAGE_CODE, MessageCode.SIGN_IN_SUCCESS)); 
    	
    }
    
    // 김기찬이 추가 출석
    @GetMapping("/attendance-dates")
    public ResponseEntity<List<LocalDate>> getAttendanceDates(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<LocalDate> attendanceDates = attendance_service.getAttendanceDates(email);
        return ResponseEntity.ok(attendanceDates);
    }
    
    // 이메일 중복 확인
    @GetMapping("/email-check")
    public ResponseEntity<Map<String, String>> emailCheck(
    		@RequestParam("email")
    		@NotBlank(message = MessageCode.EMAIL_NOT_BLANK)
    		@Email(message = MessageCode.EMAIL_PATTERN_INVALID)
    		String email
    ){
    	
    	member_service.emailCheck(email);	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.EMAIL_CAN_USE));
    	
    }
    
    // 닉네임 중복 확인
    @GetMapping("/nickname-check")
    public ResponseEntity<Map<String, String>> nicknameCheck(
    		@RequestParam("nickname")
    		@NotBlank(message = MessageCode.NICKNAME_NOT_BLANK)
    		@Size(min = 2, max = 20, message = MessageCode.NICKNAME_SIZE_INVALID)
    		@Pattern(regexp = "^[a-zA-Z가-힣\\d]{2,20}$", message = MessageCode.NICKNAME_PATTERN_INVALID)
    		String nickname
    ){
    	
    	member_service.nicknameCheck(nickname);
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.NICKNAME_CAN_USE));
    	
    }
    
    // 회원 가입
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody MemberDto.SignUp sign_up){
    	
    	member_service.signUp(sign_up);
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS));

    }
    
	// 회원 정보 수정
	@PatchMapping("/modify-info")
	public ResponseEntity<Map<String, String>> modifyInfo(
			@Valid 
			@RequestBody MemberDto.Modify modify, 
			@AuthenticationPrincipal UserDetails user_details
	){
		
		// 현재 로그인 중인 아이디 가져오기
		String current_member_email = user_details.getUsername();
		
		member_service.modifyInfo(current_member_email, modify);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_INFO_MODIFY_SUCCESS));
		
	}
	
	// 회원 탈퇴
	@DeleteMapping("/withdraw-member")
	public ResponseEntity<Map<String, String>> withdrawMember(@AuthenticationPrincipal UserDetails user_details){
		
		// 현재 로그인 중인 아이디 가져오기
		String current_member_id = user_details.getUsername();
		// 회원 탈퇴
		member_service.withdrawMember(current_member_id);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.MEMBER_WITHDRAW_SUCCESS));
		
	}
	
	// 비밀번호 재설정(링크)
	@PatchMapping("/password-reset-by-link")
	public ResponseEntity<Map<String, String>> passwordResetByLink(@Valid @RequestBody MemberDto.PasswordReset password_reset){
		
		// 비밀번호 재설정
		member_service.passwordResetByLink(password_reset.getTemporal_token(), password_reset.getNew_password());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PASSWORD_RESET_SUCCESS));
		
	}
	
	// 소셜 회원가입
	@PostMapping("/social-sign-up")
	public ResponseEntity<Map<String, String>> socialSignUp(@Valid @RequestBody MemberDto.SocialSignUp social_sign_up){
		
		// 소셜 회원가입
		String email = member_service.socialSignUp(social_sign_up);
		
		// 출석 체크
		attendance_service.recordAttendance(email);
		
		final UserDetails user_details = auth_service.loadUserByUsername(email);
		final String jwt = jwt_util.generateAcessToken(user_details);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(Name.ACCESS_TOKEN.toString(), jwt, MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS));
		
	}
	
	// 프로필 이미지 업로드
	@PostMapping("/upload-profile-image")
	public ResponseEntity<Map<String, String>> uploadProfileImage(
			@Valid @RequestParam("profile_image_file") MultipartFile profile_image_file,
			@AuthenticationPrincipal UserDetails user_details
	){
		
		file_service.uploadProfileImage(user_details.getUsername(), profile_image_file);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PROFILE_IMAGE_UPLOAD_SUCCESS));
		
	}
	
	// 프로필 이미지 삭제
	@DeleteMapping("/delete-profile-image")
	public ResponseEntity<Map<String, String>> deleteProfileImage(@AuthenticationPrincipal UserDetails user_details){
		
		file_service.deleteProfileImage(user_details.getUsername());
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PROFILE_IMAGE_DELETE_SUCCESS));
		
	}
	
	// 프로필 이미지 불러오기
	@GetMapping("/get-profile-image")
	public ResponseEntity<Resource> getProfileImage(
			@AuthenticationPrincipal UserDetails user_details,
			HttpServletRequest http_servlet_request
	){
		
		// 이미지 파일 불러오기
		Resource resource = file_service.getProfileImage(user_details.getUsername());
		String content_type = null;
		
		try {
			
			content_type = http_servlet_request.getServletContext()
					.getMimeType(resource.getFile().getAbsolutePath());
		
		} catch (IOException ex) {
			
			content_type = "application/octet-stream";
			
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.contentType(MediaType.parseMediaType(content_type))
				.body(resource);
		
	}
	
	
	

}
