package com.blsa.ezilog.config.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private ObjectMapper objectMapper;

    private byte[] httpRequestBodyByteArray;
    private ByteArrayInputStream bis;

    public CustomRequestWrapper(ServletRequest request) {
        super((HttpServletRequest) request);
        this.objectMapper = new ObjectMapper();

        try {
            this.httpRequestBodyByteArray = StreamUtils.copyToByteArray(request.getInputStream());
            this.bis = new ByteArrayInputStream(httpRequestBodyByteArray);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                return;
            }

            @Override
            public int read() {
                return bis.read();
            }
        };
    }

    public Object convertToObject() throws IOException {
        if (httpRequestBodyByteArray.length == 0)
            return null;
        return objectMapper.readValue(httpRequestBodyByteArray, Object.class);
    }
}