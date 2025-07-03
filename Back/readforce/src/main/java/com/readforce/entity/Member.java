package com.readforce.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.readforce.enums.Role;
import com.readforce.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
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
public class Member {
	
	@Id
	@Email
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true)
	private String nickname;
	
	@Column(nullable = false)
	private LocalDate birthday;
	
	@Column(name = "profile_image_url", nullable = true)
	private String profile_image_url;
	
	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private LocalDateTime create_date;
	
	@LastModifiedDate
	@Column(name = "last_modified_date")
	private LocalDateTime last_modified_date;
	
	@Column(name = "withdraw_date", nullable = true)
	private LocalDateTime withdraw_date;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;
	
	@Column(name = "social_provider", nullable = true)
	private String social_provider;
	
	@Column(name = "social_provider_id", nullable = true)
	private String social_provider_id;
	
	@Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Member member = (Member) object;
        return email != null && email.equals(member.email);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
	
}
