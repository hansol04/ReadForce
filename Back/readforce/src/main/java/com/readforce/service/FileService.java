package com.readforce.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.readforce.entity.Member;
import com.readforce.enums.MessageCode;
import com.readforce.enums.Status;
import com.readforce.exception.FileException;
import com.readforce.exception.ResourceNotFoundException;
import com.readforce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	@Value("${file.image.profile.upload-dir}")
	private String profile_image_upload_dir;
	
	@Value("${file.image.profile.max-size}")
	private Long profile_image_max_file_size;
	
	@Value("#{'${file.image.profile.allowed-mime-types}'.split(',')}")
	private List<String> profile_image_allowed_mime_type_list;	
	
	@Value("${file.image.profile.default-image-url}")
	private String profile_default_image_url;
	
	private final MemberRepository member_repository;
	
	
	// 디렉토리를 동적으로 생성
	private Path getPath(String upload_dir) {
		
		Path path = Paths.get(upload_dir).toAbsolutePath().normalize();
		try {
			
			Files.createDirectories(path);
			return path;
			
		} catch (Exception exception) {
			
			throw new FileException(MessageCode.DIRECTORY_CREATION_FAIL);
			
		}
	
	}
	
	// 파일 유효성 검사
	private void validateFile(MultipartFile multipart_file, long max_size, List<String> type_list) {
		
		// 빈 값 확인
		if(multipart_file.isEmpty()) {
			
			throw new FileException(MessageCode.FILE_NOT_NULL);
			
		}
		
		// 사이즈 확인
		if(multipart_file.getSize() > max_size) {
			
			throw new FileException(MessageCode.FILE_SIZE_INVALID);
			
		}
		
		// 타입 확인
		String mime_type = multipart_file.getContentType();
		if(mime_type == null || !type_list.contains(mime_type)) {
			
			throw new FileException(MessageCode.FILE_TYPE_INVALID);
			
		}
		
	}
	
	// 파일 저장
	private void storeFile(MultipartFile multipart_file, String file_name, String upload_dir) {
		
		// 파일 유효성 검사
		validateFile(multipart_file, profile_image_max_file_size, profile_image_allowed_mime_type_list);
		
		try {
			
			Path target_location = this.getPath(upload_dir).resolve(file_name);
			Files.copy(multipart_file.getInputStream(), target_location, StandardCopyOption.REPLACE_EXISTING);
		
		} catch (Exception exception) {
			
			throw new FileException(MessageCode.FILE_STORE_FAIL);
			
		}
		
	}
	
	// 파일명 생성
	private String generateFileName(MultipartFile multipart_file) {
		
		String original_file_name = StringUtils.cleanPath(multipart_file.getOriginalFilename());
		String extension = original_file_name.substring(original_file_name.lastIndexOf("."));
		String file_name = UUID.randomUUID().toString() + extension;
		
		return file_name;
		
	}
	
	// 파일 불러오기
	public Resource loadFileAsResource(String file_name, String upload_dir) {
		
		try {
			Path file_path = this.getPath(upload_dir).resolve(file_name).normalize();
			Resource resource = new UrlResource(file_path.toUri());
			if(resource.exists()) {
				return resource;
			} else {
				throw new ResourceNotFoundException(MessageCode.FILE_NOT_FOUND); 
			}
		} catch (MalformedURLException exception) {
			
			throw new ResourceNotFoundException(MessageCode.FILE_NOT_FOUND);
			
		} catch (Exception exception) {
			
			throw new ResourceNotFoundException(MessageCode.FILE_NOT_FOUND);
			
		}
		
	}
	
	// 파일 삭제
	public void deleteFile(String file_name, String upload_dir) {
		
		try {
			Path file_path = this.getPath(upload_dir).resolve(file_name).normalize();
			Files.deleteIfExists(file_path);
		} catch (IOException exception) {
			throw new FileException(MessageCode.FILE_DELETE_FAIL);
		} catch (Exception exception) {
			throw new FileException(MessageCode.FILE_DELETE_FAIL);
		}
		
	}
	
	// 프로필 이미지 업로드
	@Transactional
	public void uploadProfileImage(String email, MultipartFile multipart_file) {
		
		// 회원 정보 조회
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE)
								 .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		// 기존 이미지 파일 경로
		String old_profile_image_url = member.getProfile_image_url();
		
		// 파일명 생성
		String file_name = generateFileName(multipart_file);
		
		// 회원 정보 수정
		member.setProfile_image_url(file_name);
		
		// 파일 저장
		storeFile(multipart_file, file_name, profile_image_upload_dir);
		
		// 기존 이미지가 있으면 삭제
		if(old_profile_image_url != null && !old_profile_image_url.isEmpty()) {
			deleteFile(old_profile_image_url, profile_image_upload_dir);
		}
		
	}
	
	// 프로필 이미지 불러오기
	@Transactional
	public Resource getProfileImage(String email) {
		
		// 회원 정보 조회
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE)
								 .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));

		if(member.getProfile_image_url() == null || member.getProfile_image_url().isEmpty()) {

			return loadFileAsResource(profile_default_image_url, profile_image_upload_dir);
			
		} else {
			
			return loadFileAsResource(member.getProfile_image_url(), profile_image_upload_dir);
		
		}
		
	}
	
	// 프로필 이미지 삭제
	@Transactional
	public void deleteProfileImage(String email) {
		
		// 회원 정보 조회
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE)
								 .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		String profile_image_url = member.getProfile_image_url();
		
		if(profile_image_url == null || profile_image_url.isEmpty()) {
			throw new ResourceNotFoundException(MessageCode.PROFILE_IMAGE_URL_NOT_FOUND);
		} else {
			
			// 회원 정보 변경
			member.setProfile_image_url(null);
			
			// 프로필 이미지 파일 삭제 
			deleteFile(profile_image_url, profile_image_upload_dir);			
		
		}
		
	}
	

	
	
	
	
	
	
	
}
