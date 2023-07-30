package com.gsteren.glchallenge.controllers;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsteren.glchallenge.dto.LoginResponseDTO;
import com.gsteren.glchallenge.dto.UserInputDTO;
import com.gsteren.glchallenge.dto.UserResponseDTO;
import com.gsteren.glchallenge.security.JwtUtil;
import com.gsteren.glchallenge.services.UserService;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    JwtUtil jwtUtil;
    
	@Value("${jwt.expire}")
	public int expiryTime;
    
	@PostMapping("/login")
	public ResponseEntity<Object> login() throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String username = userDetails.getUsername();
		 LoginResponseDTO user = userService.findUserByEmail(username);
		 user.setToken(jwtUtil.generateToken(username,expiryTime));
	     return ResponseEntity.ok(user);
	}

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserInputDTO userInput) {
      //TODO Validate password and email
	     UserResponseDTO createdUser = userService.createUser(userInput);
	     createdUser.setToken(jwtUtil.generateToken(userInput.getEmail(),expiryTime));
	     return ResponseEntity.ok(createdUser);
    }
   
}
