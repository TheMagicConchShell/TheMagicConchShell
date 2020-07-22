package com.blsa.ezilog.model.notice;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	@Id
	@Column(name="nid")
	private BigInteger nid;
	
	@Column (name = "title")
	private String title;
	
	@Column (name = "content")
	private String content;
	
	@Column (name = "writer")
	private String writer;
	
}
