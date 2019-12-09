package com.example.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
@WebFilter(filterName = "Filter10", urlPatterns = {"/*"})
public class Filter10 implements Filter {
    private Set<String> toExclude = new HashSet<String>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //toExclude.add("/login");
    }
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 1 - encoding begins");
        HttpServletRequest request = (HttpServletRequest) req;
        // 请求的uri
        String uri = request.getRequestURI();;
        if (!toExclude.contains(uri)) {
            //设置响应字符编码为UTF-8
            resp.setContentType("text/html;charset=UTF-8");
            if("POST-PUT".contains(request.getMethod())){
            //if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
            }
        }
        filterChain.doFilter(req, resp);//执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        System.out.println("Filter 1 - encoding ends");
        }
    }
