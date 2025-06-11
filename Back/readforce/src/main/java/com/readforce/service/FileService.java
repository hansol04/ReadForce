package com.readforce.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	@Value("${file.upload-dir}")
	private final String upload_dir;
	private final MemberRepository member_repository;
	private Path file_storage_location;
	
	// 디렉토리 생성
	@PostConstruct
	public void init() {
		
		file_storage_location = Paths.get(upload_dir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.file_storage_location);
		} catch (Exception exception) {
			throw new FileException(MessageCode.DIRECTORY_CREATION_FAIL);
		}
	}
	
	// 파일 저장
	public String storeFile(MultipartFile multipart_file) {
		
		String original_file_name = StringUtils.cleanPath(multipart_file.getOriginalFilename());
		String extension = original_file_name.substring(original_file_name.lastIndexOf("."));
		String stored_file_name = UUID.randomUUID().toString() + extension;
		
		try {
			Path target_location = this.file_storage_location.resolve(stored_file_name);
			Files.copy(multipart_file.getInputStream(), target_location, StandardCopyOption.REPLACE_EXISTING);
			return stored_file_name;
		} catch (Exception exception) {
			throw new FileException(MessageCode.FILE_STORE_FAIL);
		}
		
	}
	
	// 파일 불러오기
	public Resource loadFileAsResource(String file_name) {
		
		try {
			Path file_path = this.file_storage_location.resolve(file_name).normalize();
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
	public void deleteFile(String file_name) {
		
		try {
			Path file_path = this.file_storage_location.resolve(file_name).normalize();
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
		
		// 기존 이미지가 있으면 삭제
		if(member.getProfile_image_url() != null && !member.getProfile_image_url().isEmpty()) {
			deleteFile(member.getProfile_image_url());
		}
		
		String file_name = storeFile(multipart_file);
		member.setProfile_image_url(file_name);
		
	}
	
	// 프로필 이미지 삭제
	@Transactional
	public void deleteProfileImage(String email) {
		
		// 회원 정보 조회
		Member member = 
				member_repository.findByEmailAndStatus(email, Status.ACTIVE)
								 .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));
		
		if(member.getProfile_image_url() == null || member.getProfile_image_url().isEmpty()) {
			throw new ResourceNotFoundException(MessageCode.PROFILE_IMAGE_URL_NOT_FOUND);
		} else {
			deleteFile(member.getProfile_image_url());
			member.setProfile_image_url(null);
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
			throw new ResourceNotFoundException(MessageCode.PROFILE_IMAGE_URL_NOT_FOUND);
		} else {
			return loadFileAsResource(member.getProfile_image_url());
		}
		
	}

	
	
	
	
	
	
	
}
