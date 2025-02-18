package com.heima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.heima.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//检查用户是否登录的过滤器
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/common/**"
        };
        //2，判断是否需要处理
        boolean check = check(urls, requestURI);
        //3.如果不需要处理就直接放行
        if(check){
            filterChain.doFilter(request, response);
            return;
        }
        //4.判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            filterChain.doFilter(request, response);
            return;
        }
        //5.若未登录则返回登录结果：向客户端发送请求
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    //路径匹配检查本次请求是否需要放行
    public boolean check(String[] urls,String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
