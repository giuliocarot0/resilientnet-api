package com.resilientnet.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, @org.jetbrains.annotations.NotNull FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Arrays.stream(request.getCookies()).forEach(c->{
            System.out.println(c.getName() + "=" +c.getValue());
        });

        //doFilter
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
