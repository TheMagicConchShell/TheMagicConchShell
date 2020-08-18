package com.blsa.ezilog.config.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blsa.ezilog.model.log.RequestLog;
import com.blsa.ezilog.model.log.ResponseLog;

public class LoggingUtil {
    public static RequestLog makeLoggingRequestMap(final HttpServletRequest request) {
        // request info
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("request-url", request.getRequestURL().toString());
        requestMap.put("query-string", request.getQueryString());
        requestMap.put("method", request.getMethod());
        requestMap.put("remote-addr", request.getRemoteAddr());
        requestMap.put("remote-host", request.getRemoteHost());
        requestMap.put("remote-port", request.getRemotePort());
        requestMap.put("remote-user", request.getRemoteUser());
        requestMap.put("character-encoding", request.getCharacterEncoding());

        RequestLog rqlog = new RequestLog();
        Map<String, Object> rqHeader = new HashMap<>();

        LocalDateTime current = LocalDateTime.now();
        rqlog.setRequestUrl(request.getRequestURI());
        rqlog.setQueryString(request.getQueryString());
        rqlog.setMethod(request.getMethod());
        rqlog.setRemoteAddr(request.getRemoteAddr());
        rqlog.setRemoteHost(request.getRemoteHost());
        Integer port = request.getRemotePort();
        rqlog.setRemotePort(port.toString());
        rqlog.setRemoteUser(request.getRemoteUser());
        rqlog.setCharacterEncoding(request.getCharacterEncoding());
        rqlog.setBody((String) requestMap.get("body"));
        rqlog.setTimestamp(current);

        // request header
        Map<String, Object> requestHeaderMap = new HashMap<>();
        Enumeration<String> requestHeaderNameList = request.getHeaderNames();
        while (requestHeaderNameList.hasMoreElements()) {
            String headerName = requestHeaderNameList.nextElement();
            requestHeaderMap.put(headerName, request.getHeader(headerName));
        }
        rqlog.setJwtAuthToken((String) requestHeaderMap.get("jwt-auth-token"));
        rqlog.setNickname((String) requestHeaderMap.get("nickname"));
        rqlog.setRefererHttp((String) requestHeaderMap.get("referer"));
        rqlog.setOrigin((String) requestHeaderMap.get("origin"));

        // request Body
        try {
            Object requestBody = ((CustomRequestWrapper) request).convertToObject();
            if (requestBody==(null)) {
                rqlog.setBody(null);
            } else {
                rqlog.setBody(requestBody.toString());
            }
            // rqlog.setBody((String)requestBody);
        } catch (IOException ignored) {
        }

        return rqlog;
    }

    public static ResponseLog makeLoggingResponseMap(final HttpServletResponse response, Long request_id)
            throws IOException {
        // response info
        LocalDateTime current = LocalDateTime.now();

        ResponseLog rslog = new ResponseLog();
        Integer status = response.getStatus();
        rslog.setStatus(status.toString());
        rslog.setTimestamp(current);

        // response header
        Map<String, Object> responseHeaderMap = new HashMap<>();
        Collection<String> responseHeaderNameList = response.getHeaderNames();
        responseHeaderNameList.forEach(v -> responseHeaderMap.put(v, response.getHeader(v)));
        rslog.setHeader(responseHeaderNameList.toString());

        rslog.setRequest_id(request_id);

        return rslog;
    }
}
