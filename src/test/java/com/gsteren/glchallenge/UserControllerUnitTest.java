package com.gsteren.glchallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.gsteren.glchallenge.controllers.UserController;
import com.gsteren.glchallenge.dto.LoginResponseDTO;
import com.gsteren.glchallenge.dto.UserInputDTO;
import com.gsteren.glchallenge.dto.UserResponseDTO;
import com.gsteren.glchallenge.exception.CustomException;
import com.gsteren.glchallenge.security.JwtUtil;
import com.gsteren.glchallenge.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void testSignUp() {
        // Prepare test data
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setEmail("test@example.com");
        // Set properties of userInputDTO

        UserResponseDTO createdUserResponse = new UserResponseDTO();
        // Set properties of createdUserResponse

        // Mock userService.createUser() method
        when(userService.createUser(any(UserInputDTO.class))).thenReturn(createdUserResponse);

        // Mock jwtUtil.generateToken() method
        when(jwtUtil.generateToken(anyString(), anyInt())).thenReturn("dummyToken");

        // Perform the POST request
        ResponseEntity<?> responseEntity = userController.signUp(userInputDTO);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof UserResponseDTO);

        UserResponseDTO responseDTO = (UserResponseDTO) responseEntity.getBody();
        assertNotNull(responseDTO.getToken());
        // Add more assertions based on the expected properties of the response
    }

    @Test
    public void testLogin() throws Exception {
        // Prepare test data
        String username = "test@example.com";
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = mock(UserDetails.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        // Set properties of loginResponseDTO

        // Mock userService.findUserByEmail() method
        when(userService.findUserByEmail(username)).thenReturn(loginResponseDTO);

        // Mock jwtUtil.generateToken() method
        when(jwtUtil.generateToken(anyString(), anyInt())).thenReturn("dummyToken");

        // Perform the POST request
        ResponseEntity<Object> responseEntity = userController.login();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof LoginResponseDTO);

        LoginResponseDTO responseDTO = (LoginResponseDTO) responseEntity.getBody();
        assertNotNull(responseDTO.getToken());
        // Add more assertions based on the expected properties of the response
    }
    
   
}
