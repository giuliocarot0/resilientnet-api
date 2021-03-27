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
import java.util.*;

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
        Map<String, String> cookies = new HashMap<>();
        Boolean no_cookie = false;
        if(request.getCookies() == null) no_cookie = true;
        else {
            Arrays.stream(request.getCookies()).forEach(c -> {
                cookies.put(c.getName(), new String(Base64.getDecoder().decode(c.getValue().getBytes())));
            });
        }
        String auth = request.getHeader("Authorization");
        if (auth == null || no_cookie) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
        } else {
            String type = auth.split(" ")[0];
            String token = auth.split(" ")[1];
            if (!type.equals("Bearer")) {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong Grant Type");
            }
            else {
                if (jwtTokenUtils.validateToken(token, cookies.get("_uid")))
                //doFilter
                    filterChain.doFilter(request, servletResponse);
                else
                    ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            }
        }
    }
    @Override
    public void destroy() {

    }
}
