package com.readforce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.readforce.dto.MemberDto.GetMemberObject;
import com.readforce.dto.MemberDto.MemberAttemptedQuiz;
import com.readforce.dto.MemberDto.MemberIncorrectQuiz;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Name;
import com.readforce.exception.AuthenticationException;
import com.readforce.service.AttendanceService;
import com.readforce.service.AuthService;
import com.readforce.service.FileService;
import com.readforce.service.MemberService;
import com.readforce.service.QuizService;
import com.readforce.util.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
@Slf4j
public class MemberController {
	
	private final MemberService member_service;
	private final AuthService auth_service;
	private final AuthenticationManager authentication_manager;
	private final JwtUtil jwt_util;
	private final FileService file_service;
	private final AttendanceService attendance_service;
	private final PasswordEncoder password_encoder;
	private final QuizService quiz_service;
	
	@Value("${custom.fronted.kakao-logout-url}")
	private String custom_fronted_kakao_logout_url;
	
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakao_client_id;
	
	@Value("${custom.fronted.logout-redirect-url}")
	private String custom_fronted_logout_redirect_url;
	
	// 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@Valid @RequestBody MemberDto.SignIn sign_in){	
    	
    	// 사용자 인증
    	authentication_manager.authenticate(
    			new UsernamePasswordAuthenticationToken(sign_in.getEmail(), sign_in.getPassword())
    	);
    	
    	// 출석 체크
    	attendance_service.recordAttendance(sign_in.getEmail());
    	
    	// 엑세스 토큰, 리프레쉬 토큰 생성
    	final UserDetails user_details = auth_service.loadUserByUsername(sign_in.getEmail());
    	final String access_token = jwt_util.generateAcessToken(user_details);
    	final String refresh_token = jwt_util.generateRefreshToken(user_details);
    	
    	// 회원 조회
    	GetMemberObject get_member_dto = member_service.getMemberObjectByEmail(user_details.getUsername());
    	
    	// 리프레쉬 토큰 저장
    	auth_service.storeRefreshToken(user_details.getUsername(), refresh_token);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(Map.of(
    			Name.ACCESS_TOKEN.toString(), access_token,
    			Name.REFRESH_TOKEN.toString(), refresh_token,
    			Name.NICK_NAME.toString(), get_member_dto.getNickname(),
    			Name.PROVIDER.toString(), get_member_dto.getProvider() == null ? "" : get_member_dto.getProvider(),
    			MessageCode.MESSAGE_CODE, MessageCode.SIGN_IN_SUCCESS
    	)); 
    	
    }
    
    // 로그아웃
    @DeleteMapping("/sign-out")
    public ResponseEntity<Map<String, String>>signOut(@AuthenticationPrincipal UserDetails user_details){
    	
    	String email = user_details.getUsername();   	
    	
    	// 리프레쉬 토큰 삭제
    	auth_service.deleteRefreshToken(user_details.getUsername());
    	
    	// 회원 조회
    	GetMemberObject member_info = member_service.getMemberObjectByEmail(email);
    	String provider = member_info.getProvider();
    	
    	// 응답 본문 생성
    	Map<String, String> response_body = new HashMap<>();
    	response_body.put(MessageCode.MESSAGE_CODE, MessageCode.SIGN_OUT_SUCCESS);
    	
    	// 카카오 소셜 계정
    	if("kakao".equals(provider)) {
    		
    		String kakao_sign_out_url = custom_fronted_kakao_logout_url
    				+ kakao_client_id
    				+ "&logout_redirect_uri="
    				+ custom_fronted_logout_redirect_url;
    		
    		response_body.put(Name.KAKAO_SIGN_OUT_URL.toString(), kakao_sign_out_url);
    		
    	}
    	
    	return ResponseEntity.status(HttpStatus.OK).body(response_body); 
    	
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
    
    // 일반 회원 가입
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody MemberDto.SignUp sign_up){
    	
    	// 일반 회원 가입
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
		
		// 회원 정보 조회
		GetMemberObject member_info = member_service.getMemberObjectByEmail(current_member_email);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.MEMBER_INFO_MODIFY_SUCCESS,
				Name.NICK_NAME.toString(), member_info.getNickname()
		));
		
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
	public ResponseEntity<Map<String, String>> passwordResetByLink(@Valid @RequestBody MemberDto.PasswordResetByLink password_reset_by_link){
		
		// 비밀번호 재설정
		member_service.passwordResetByLink(password_reset_by_link.getTemporal_token(), password_reset_by_link.getNew_password(), password_reset_by_link.getBirthday());
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(MessageCode.MESSAGE_CODE, MessageCode.PASSWORD_RESET_SUCCESS));
		
	}
	
	// 비밀번호 재설정(회원 정보 수정)
	@PatchMapping("/password-reset-by-site")
	public ResponseEntity<Map<String, String>> passwordResetBySite(
			@Valid @RequestBody MemberDto.PasswordResetBySite password_reset_by_site,
			@AuthenticationPrincipal UserDetails user_details
	){
		
		// 기존 비밀번호 확인
		if(!password_encoder.matches(password_reset_by_site.getOld_password(), user_details.getPassword())) {
			
			throw new AuthenticationException(MessageCode.AUTHENTICATION_FAIL);
		
		};
		
		// 비밀번호 재설정
		member_service.changePassword(user_details.getUsername(), password_reset_by_site.getNew_password());
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.PASSWORD_RESET_SUCCESS
		));
				
	}
	
	// 소셜 회원가입
	@PostMapping("/social-sign-up")
	public ResponseEntity<Map<String, String>> socialSignUp(@Valid @RequestBody MemberDto.SocialSignUp social_sign_up){
		
		// 소셜 회원가입
		member_service.socialSignUp(social_sign_up);
				
		return ResponseEntity.status(HttpStatus.OK).body(Map.of(
				MessageCode.MESSAGE_CODE, MessageCode.SIGN_UP_SUCCESS
		));
		
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
			@AuthenticationPrincipal UserDetails user_details
	){
		
		// 이미지 파일 불러오기
		Resource resource = file_service.getProfileImage(user_details.getUsername());
		String content_type = null;
		
		try {
			
			Path file_path = resource.getFile().toPath();
			content_type = Files.probeContentType(file_path);
			
			if(content_type == null) {
				
				content_type = "application/octet-stream";
				log.warn("이미지 파일 타입을 결정하지 못했습니다. {}, 기본 타입인 application/octet-stream 타입으로 결정되었습니다.", resource.getFilename());
				
			}
		
		} catch (IOException ex) {
			
			content_type = "application/octet-stream";
			log.warn("이미지 파일에 접근하지 못하여 타입을 결정하지 못했습니다. {}, 기본 타입인 application/octet-stream 타입으로 결정되었습니다.", resource.getFilename());
			
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
				.contentType(MediaType.parseMediaType(content_type))
				.body(resource);
		
	}
	
	// 출석 확인
	@GetMapping("/get-attendance-date-list")
	public ResponseEntity<List<LocalDate>> getAttendanceDateList(@AuthenticationPrincipal UserDetails user_details){
		
		String email = user_details.getUsername();
		
		List<LocalDate> getAttendanceDateList = attendance_service.getAttendanceDateList(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(getAttendanceDateList);
	}
	
	// 사용자가 푼 최근 문제 10개 가져오기
	@GetMapping("/get-member-solved-quiz-list-10")
	public ResponseEntity<List<MemberAttemptedQuiz>> getMemberSolvedQuizList10(@AuthenticationPrincipal UserDetails user_details){
		
		String email = user_details.getUsername();
		
		// 사용자가 푼 최근 문제 10개 가져오기
		List<MemberAttemptedQuiz> member_attempted_quiz_list = quiz_service.getMemberSolvedQuizList10(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(member_attempted_quiz_list);
		
	}
	
	// 틀린 문제 가져오기(최신순)
	@GetMapping("/get-member-incorrect-quiz-list")
	public ResponseEntity<List<MemberIncorrectQuiz>> getMemberIncorrectQuizList(@AuthenticationPrincipal UserDetails user_details){
		
		String email = user_details.getUsername();
		
		// 틀린 문제 가져오기
		List<MemberIncorrectQuiz> member_incorrect_quiz_list = quiz_service.getMemberIncorrectQuizList(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(member_incorrect_quiz_list);
		
		
	}

	
	
	

}
