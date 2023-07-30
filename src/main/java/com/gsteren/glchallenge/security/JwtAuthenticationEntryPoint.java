package com.gsteren.glchallenge.security;


import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gsteren.glchallenge.dto.ErrorDTO;

//https://www.devglan.com/spring-security/exception-handling-in-spring-security
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You're not authorized to perform this transaction.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        You can also write JSON object below to send proper response as you send from REST resources.
        
          ObjectMapper objectMapper =new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
          objectMapper.registerModule(new JavaTimeModule());
          // Configure the date-time format for LocalDateTime
          objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"));

          objectMapper.writeValue(response.getOutputStream(),ErrorDTO.createErrorDTO(HttpServletResponse.SC_UNAUTHORIZED, "You're not authorized to perform this operation"));
    }
}