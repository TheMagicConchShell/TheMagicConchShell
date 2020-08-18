package com.blsa.ezilog.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.blsa.ezilog.config.util.CustomRequestWrapper;
import com.blsa.ezilog.config.util.CustomResponseWrapper;
import com.blsa.ezilog.config.util.LoggingUtil;
import com.blsa.ezilog.dao.RequestLogDao;
import com.blsa.ezilog.dao.ResponseLogDao;
import com.blsa.ezilog.model.log.RequestLog;
import com.blsa.ezilog.model.log.ResponseLog;

@Component
public class RequestResponseLoggerFilter extends GenericFilterBean {
    @Autowired
    ResponseLogDao responseLogDao;

    @Autowired
    RequestLogDao requestLogDao;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Pattern pattern = Pattern.compile("multipart");
        boolean isMultipart = request.getContentType() == null ? false : pattern.matcher(request.getContentType()).find();
        
        if (request.getContentType() != null && isMultipart) {
            chain.doFilter(request, response);
            
        } else {
            final HttpServletRequest wrapper = new CustomRequestWrapper(request);

            RequestLog rqlog = LoggingUtil.makeLoggingRequestMap(wrapper);

            chain.doFilter(wrapper, response);

            final HttpServletResponse res = new CustomResponseWrapper(response);

            Map<String, Object> rqHeader = new HashMap<>();
            ResponseLog rplog = new ResponseLog();

            try {
                requestLogDao.save(rqlog);

                Optional<RequestLog> optrequest = requestLogDao.getLastInsert();

                if (optrequest.isPresent()) {
                    RequestLog last = optrequest.get();
                    ResponseLog rslog = LoggingUtil.makeLoggingResponseMap(res, last.getId());
                    responseLogDao.save(rslog);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

    }

}
