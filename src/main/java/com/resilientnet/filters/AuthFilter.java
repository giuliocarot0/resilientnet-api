package com.resilientnet.filters;

import com.resilientnet.api.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Locale;

@Component
public class AuthFilter implements Filter {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, @org.jetbrains.annotations.NotNull FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if ("/basic/authenticate".equals(request.getRequestURI())) {
            filterChain.doFilter(request, servletResponse);
            return;
        }

        Arrays.stream(request.getCookies()).forEach(c->{
            System.out.println(c.getName() + "=" +c.getValue());
           // System.out.println(Base64.getDecoder().decode(c.getValue().getBytes()).toString());
        });

        String auth = request.getHeader("Authorization");
        String token = auth.split(" ")[1];

        if(jwtTokenUtils.validateToken(token, request.getCookies()[0].getValue()))
        //doFilter
            filterChain.doFilter(request, servletResponse);
        else
            ((HttpServletResponse)servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid token");
    }

    @Override
    public void destroy() {

    }
}
