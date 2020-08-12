package com.blsa.ezilog.exception.auth;

import lombok.Getter;

@Getter
public class FrobiddenResponse {
    String timestamp;
    String path;
    String required;
    String provided;
    String message;
    
    public FrobiddenResponse(
            String timestamp, 
            String path, 
            String required, 
            String provided, 
            String message) {
        super();
        this.timestamp = timestamp;
        this.path = path;
        this.required = required;
        this.provided = provided;
        this.message = message;
    }

    public static FrobiddenResponse of(ForbiddenException e) {
        return new FrobiddenResponse(
                e.timestamp,
                e.path,
                e.required,
                e.provided,
                e.getMessage());
    }
}
