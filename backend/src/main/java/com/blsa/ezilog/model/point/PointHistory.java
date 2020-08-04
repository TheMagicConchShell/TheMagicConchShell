package com.blsa.ezilog.model.point;

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
@AllArgsConstructor
@NoArgsConstructor

public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger id;

    @Column
    BigInteger uid;

    @Column
    LocalDateTime stamp;

    @Column(name="change_val")
    int change;

    @Column
    String reason;

    public PointHistory(BigInteger uid, LocalDateTime timestamp, int change, String reason) {
        super();
        this.uid = uid;
        this.stamp = timestamp;
        this.change = change;
        this.reason = reason;
    }

}
