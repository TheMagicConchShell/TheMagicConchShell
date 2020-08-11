package com.blsa.ezilog.model.rank;

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
public class UserRank {
    @Id
    private String nickname;
    @Column(name = "reply_count")
    private int replyCount;
    @Column(name = "post_count")
    private int postCount;
    @Column
    private int point;
    @Column
    private int level;
}
