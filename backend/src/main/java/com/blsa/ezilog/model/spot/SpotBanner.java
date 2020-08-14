package com.blsa.ezilog.model.spot;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpotBanner {
    private Long no;
    private String title;
    public SpotBanner(Long no, String title) {
        super();
        this.no = no;
        this.title = title;
    }
    
    
    
}
