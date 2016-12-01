package com.tl.ticker.web.config.filter;

import com.tl.ticker.web.common.Constant;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pangjian on 16-12-1.
 */
@WebFilter(displayName = "securityFilter",urlPatterns = "*/admin/**")
public class SecurityFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("P3P: CP", "\"CAO PSA OUR\"");

        String home = request.getContextPath();
        String uri = request.getRequestURI();//当前访问的地址

        if(uri.indexOf("/js/")>=0 ||uri.indexOf("/images/")>=0 ||uri.indexOf(".css")>=0 ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getSession().getAttribute(Constant.SESSION_USER) != null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String loginUrl = home + "/admin";

        if (uri.equals(loginUrl)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        System.out.println(loginUrl);
        response.sendRedirect(loginUrl);
    }

    @Override
    public void destroy() {

    }
}
