package com.blsa.ezilog.model.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
	@Id
	 @GeneratedValue(strategy= GenerationType.IDENTITY)
	private long uid;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@Column
	private String nickname;
	
	@Column
	private String introduce;
	
	@Column
	private String profile;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime authDate;
}
