package com.hjay.tmall.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

//@WebFilter(urlPatterns = "/*")// 这里不使用注解，因为无法表名顺序
public class BackServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("-------------- BackServletFilter Filter --------------");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // project path
        String contextPath = httpServletRequest.getServletContext().getContextPath();
        String webProjectPath = httpServletRequest.getServletContext().getRealPath("/");
        // URL without http protocol and host
        String uri = httpServletRequest.getRequestURI();
        // /admin_category_list
        String requestURI = StringUtils.remove(uri,contextPath);

        // this the dispatcher mechanism would invoke 500 error when
        // user is enter this directly:

        // http://localhost:3030/Tmall/admin_property_list?cid=68
        // 这样是无法拦截的，因为后面的是request body 而不是URI的一部分 (get method)

        //

        // admin method dispatcher
        if (requestURI.startsWith("/admin_")){
            // target: categoryServlet
            // String servlet_path = StringUtils.substringBetween(requestURI,"_","_") + "Servlet";
            // need to take care of this address

            // targetAddress: category
            String servlet_path = StringUtils.substringBetween(requestURI,"_","_") + "Servlet";// need to take care of this address
            String method = StringUtils.substringAfterLast(requestURI,"_");
            httpServletRequest.setAttribute("method",method);
            httpServletRequest.getRequestDispatcher("/"+servlet_path).forward(httpServletRequest,httpServletResponse);
            return;
        }

        // only filter the url starts with /admin_, and do not
        chain.doFilter(httpServletRequest,httpServletResponse);

    }

    @Override
    public void destroy() {

    }
}
