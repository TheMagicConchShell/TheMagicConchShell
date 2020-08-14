package com.blsa.ezilog.model.point;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    Long id;

    @Column
    Long uid;

    @Column(insertable = false, updatable = false)
    LocalDateTime stamp;

    @Column(name = "change_val")
    int change;

    @Column
    String reason;

    public PointHistory(Long uid, int change, String reason) {
        super();
        this.uid = uid;
        this.change = change;
        this.reason = reason;
    }

}
