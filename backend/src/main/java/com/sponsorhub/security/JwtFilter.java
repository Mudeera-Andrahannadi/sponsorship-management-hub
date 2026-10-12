package com.sponsorhub.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter
{
    private final JwtService jwt;

    public JwtFilter(JwtService jwt)
    {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException
    {
        String head = req.getHeader("Authorization");
        Me me = null;
        if (head != null && head.startsWith("Bearer "))
        {
            me = jwt.read(head.substring(7));
        }
        if (me != null)
        {
            List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + me.role().name()));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(me, null, roles));
        }
        chain.doFilter(req, res);
    }
}
