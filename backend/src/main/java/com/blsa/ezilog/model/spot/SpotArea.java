package com.blsa.ezilog.model.spot;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_no")
    private Long postNo;

    @Column(updatable = false, name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(updatable = false, name = "expire_date")
    private LocalDateTime expireDate;

    @Column
    private String owner;

    public SpotArea(Long postNo, LocalDateTime uploadDate, LocalDateTime expireDate, String owner) {
        super();
        this.postNo = postNo;
        this.uploadDate = uploadDate;
        this.expireDate = expireDate;
        this.owner = owner;
    }

}
