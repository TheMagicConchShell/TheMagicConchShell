package com.blsa.ezilog.model.log;

import java.sql.Timestamp;
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
@AllArgsConstructor
@NoArgsConstructor
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
 
    @Column
    String requestUrl;
    
    @Column
    String queryString;
    
    @Column
    String method;
    
    @Column
    String remoteAddr;
    
    @Column
    String remoteHost;
    
    @Column
    String remotePort;
    
    @Column
    String remoteUser;
    
    @Column
    String characterEncoding;
    
    @Column
    String jwtAuthToken;
    
    @Column
    String nickname;
    
    @Column
    String refererHttp;
    
    @Column
    String origin;
    
    @Column
    String body;
    
    @Column
    LocalDateTime timestamp;
}
