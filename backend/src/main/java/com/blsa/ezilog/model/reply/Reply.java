package com.blsa.ezilog.model.reply;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String writer;
    
    @Column(name = "post_no")
    private Long postNo;
    
    @Column
    private String content;
    
    @Column (name = "write_date")
    LocalDateTime writeDate;
    
    @Column (name = "like_count")
    private int likeCount;
    
    @Column (name = "unlike_count")
    private int unlikeCount;
    
    @Column
    private boolean selected;
    
    @Column
    private boolean secret;
    
    @Transient
    private boolean isMine;
    
    @Transient
    private boolean isAuthor;
    
    @Transient
    private int iLoveIt; // -1 싫어요 , 0 아무것도 없음 1 좋아요, 2 더 좋아요

    @Transient
    private String SHA256Name;
    
    
    public Reply(String writer, Long postNo, String content, LocalDateTime writeDate, boolean secret) {
        super();
        this.writer = writer;
        this.postNo = postNo;
        this.content = content;
        this.writeDate = writeDate;
        this.secret = secret;
    }
    
    public String saveWriterSHA256(String writer, Long postNo) {
        String result = "";
        try { 
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            String input = writer+postNo;
            sh.update(input.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0;i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString().substring(0, 7);
            this.SHA256Name = result;
           
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            this.SHA256Name = null;
            result = "SHA256Failed";
            
        }
        
        return result;
    }


   
}
