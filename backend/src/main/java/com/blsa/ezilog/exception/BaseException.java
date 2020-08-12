package com.blsa.ezilog.exception;

import java.text.SimpleDateFormat;

public class BaseException extends Exception {
    private static final long serialVersionUID = 5533728671248968104L;
    public String timestamp;
    public String path;

    public BaseException(String message) {
        this("", message);
    }

    public BaseException(String path, String message) {
        super(message);
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        this.path = path;
    }

}
