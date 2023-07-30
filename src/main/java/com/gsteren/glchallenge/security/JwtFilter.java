package com.gsteren.glchallenge.security;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gsteren.glchallenge.dto.ErrorDTO;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService service;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");

            String token = null;
            String userName = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                //            if (jwtUtil.isTokenExpired(token)) {
                //                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                //                httpServletResponse.getWriter().println("Token Expired");
                //                return;
                //            }
                userName = jwtUtil.extractUsername(token);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = service.loadUserByUsername(userName);

                if (jwtUtil.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (Exception e) {

            //httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"You're not authorized to perform this transaction." );

            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.debug("Unauthorized error: {}" +  e.getMessage());
           
            ObjectMapper objectMapper =new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.registerModule(new JavaTimeModule());
            // Configure the date-time format for LocalDateTime
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"));

            objectMapper.writeValue(httpServletResponse.getOutputStream(),ErrorDTO.createErrorDTO(HttpServletResponse.SC_UNAUTHORIZED, "You're not authorized to perform this operation"));
            httpServletResponse.getOutputStream().close();
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
