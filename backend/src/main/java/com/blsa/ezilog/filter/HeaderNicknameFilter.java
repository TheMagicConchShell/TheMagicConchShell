package com.blsa.ezilog.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class HeaderNicknameFilter extends GenericFilterBean {

    @Override
    public void doFilter(
            ServletRequest request, 
            ServletResponse response, 
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getHeader(String name) {
                if ("nickname".equals(name)) {
                    try {
                        String e = super.getHeader(name);
                        if (e != null)
                            return URLDecoder.decode(e, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return super.getHeader(name);
            }
            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("nickname".equals(name)) {
                    try {
                        Collection<String> result = new ArrayList<String>();
                        Enumeration<String> e = super.getHeaders(name);
                        while (e.hasMoreElements()) {
                            result.add(URLDecoder.decode(e.nextElement(), "UTF-8"));
                        }
                       
                        return Collections.enumeration(result);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return super.getHeaders(name);
            }
        };
        
        chain.doFilter(wrapper, response);
    }
}

