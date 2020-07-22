package com.blsa.ezilog.model.blog;

import java.math.BigInteger;
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
public class Blog {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "blog_id")
    BigInteger id;

    @Column(name = "blog_name")
    String name;

    @Column(name = "blog_url")
    String url;

    @Column(name = "blog_create_date")
    LocalDateTime createDate;

    public Blog(String name, String url, LocalDateTime createDate) {
        super();
        this.name = name;
        this.url = url;
        this.createDate = createDate;
    }
    
    
}
