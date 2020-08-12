package com.blsa.ezilog.exception.auth;

import com.blsa.ezilog.exception.BaseException;

public class ForbiddenException extends BaseException {
    private static final long serialVersionUID = -5879357429187890017L;
    
    public String required;
    public String provided;

    public ForbiddenException(String path, String message, String required, String provided) {
        super(path, message);
        this.required = required;
        this.provided = provided;
    }
}
