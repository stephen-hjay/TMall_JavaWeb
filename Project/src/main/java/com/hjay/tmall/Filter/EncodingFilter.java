package com.hjay.tmall.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private String encodingCharSet;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // read the init Param of the enconding filter
        encodingCharSet= filterConfig.getInitParameter("EncodingCharSet");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("-------------- Encoding Filter:"+encodingCharSet+" --------------");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletRequest.setCharacterEncoding(encodingCharSet);
        chain.doFilter(httpServletRequest,httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
