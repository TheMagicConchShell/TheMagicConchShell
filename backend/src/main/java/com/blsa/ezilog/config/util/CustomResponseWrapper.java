package com.blsa.ezilog.config.util;

import java.io.IOException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomResponseWrapper extends ContentCachingResponseWrapper {
    private ObjectMapper objectMapper;

    public CustomResponseWrapper(ServletResponse response) {
        super((HttpServletResponse) response);
        this.objectMapper = new ObjectMapper();
    }

    public Object convertToObject() throws IOException {
        return objectMapper.readValue(getContentAsByteArray(), Object.class);
    }
}