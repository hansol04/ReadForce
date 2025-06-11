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

@Service
public class FileService {

    private final String uploadDir;
    private final MemberRepository memberRepository;
    private Path fileStorageLocation;

    
    public FileService(@Value("${file.upload-dir}") String uploadDir,
                       MemberRepository memberRepository) {
        this.uploadDir = uploadDir;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileException(MessageCode.DIRECTORY_CREATION_FAIL);
        }
    }

    public String storeFile(MultipartFile multipartFile) {
        String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String storedFileName = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (Exception e) {
            throw new FileException(MessageCode.FILE_STORE_FAIL);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException(MessageCode.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException | RuntimeException e) {
            throw new ResourceNotFoundException(MessageCode.FILE_NOT_FOUND);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException | RuntimeException e) {
            throw new FileException(MessageCode.FILE_DELETE_FAIL);
        }
    }

    @Transactional
    public void uploadProfileImage(String email, MultipartFile multipartFile) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));

        if (member.getProfile_image_url() != null && !member.getProfile_image_url().isEmpty()) {
            deleteFile(member.getProfile_image_url());
        }

        String fileName = storeFile(multipartFile);
        member.setProfile_image_url(fileName);
    }

    @Transactional
    public void deleteProfileImage(String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));

        if (member.getProfile_image_url() == null || member.getProfile_image_url().isEmpty()) {
            throw new ResourceNotFoundException(MessageCode.PROFILE_IMAGE_URL_NOT_FOUND);
        } else {
            deleteFile(member.getProfile_image_url());
            member.setProfile_image_url(null);
        }
    }

    @Transactional
    public Resource getProfileImage(String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(MessageCode.MEMBER_NOT_FOUND_WITH_EMAIL));

        if (member.getProfile_image_url() == null || member.getProfile_image_url().isEmpty()) {
            throw new ResourceNotFoundException(MessageCode.PROFILE_IMAGE_URL_NOT_FOUND);
        } else {
            return loadFileAsResource(member.getProfile_image_url());
        }
    }
}
