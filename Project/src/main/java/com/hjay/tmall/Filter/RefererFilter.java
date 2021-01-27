package com.hjay.tmall.Filter;
import com.hjay.tmall.GlobalParameters;
import com.hjay.tmall.Utils.Error;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RefererFilter extends HttpServlet implements Filter {

    private static final long serialVersionUID = 1L;
    private FilterConfig filterConfig;

    public void init(FilterConfig config) {
        this.filterConfig = config;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws ServletException, IOException {
        System.out.println("------Referer Filter---------");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // 链接来源地址
        String referer = request.getHeader("referer");
        if (referer == null || !referer.contains(request.getServerName())) {
            /**
             * 如果 链接地址来自其他网站，则返回错误页面
             */
            request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_REQUEST));

            request.getRequestDispatcher("admin/404.jsp").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
        this.filterConfig = null;
    }

}