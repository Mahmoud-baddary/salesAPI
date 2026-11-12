package com.baddary.salesAPI.filter;

import com.baddary.salesAPI.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Collections;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow public endpoints without token
        if (path.equals("/auth/login") || path.equals("/users/first") ||
                path.equals("/users/count") || path.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                // Extract username and role from token
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                // ✅ Create Spring Security authority (ROLE_ prefix is required)
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                // ✅ Create authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                        null, Collections.singletonList(authority));

                // ✅ Store it in SecurityContext (this is what makes @PreAuthorize work)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
                return;
            } else {
                sendErrorJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }

        sendErrorJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
    }

    private void sendErrorJson(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}