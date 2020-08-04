package com.blsa.ezilog.model.point;

public class PointRank {
    String nickname;
    int totalPoint;
    public PointRank(String nickname, int totalPoint) {
        super();
        this.nickname = nickname;
        this.totalPoint = totalPoint;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public int getTotalPoint() {
        return totalPoint;
    }
    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }
    
    
}
