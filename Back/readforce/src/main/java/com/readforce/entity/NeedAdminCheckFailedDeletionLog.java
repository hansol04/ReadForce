package com.readforce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeedAdminCheckFailedDeletionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long need_admin_check_failed_deletion_log_no;
	
	private String email;
	
	@Column(name = "file_path")
	private String file_path;
	
	private String reason;
	
	@CreatedDate
	private LocalDateTime created_date;
	
	public NeedAdminCheckFailedDeletionLog(String email, String file_path, String reason) {
		
		this.email = email;
		this.file_path = file_path;
		this.reason = reason;
		
	}
	
}
