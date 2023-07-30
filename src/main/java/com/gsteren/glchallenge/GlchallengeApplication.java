package com.gsteren.glchallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gsteren.glchallenge.repositories.UserRepository;

@SpringBootApplication
public class GlchallengeApplication {
	
    @Autowired
    private UserRepository repository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(GlchallengeApplication.class, args);
	}

}
