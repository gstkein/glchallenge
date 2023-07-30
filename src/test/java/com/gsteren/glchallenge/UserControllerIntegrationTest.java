package com.gsteren.glchallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.ResourceAccessException;

import com.gsteren.glchallenge.dto.ErrorDTO;
import com.gsteren.glchallenge.dto.LoginResponseDTO;
import com.gsteren.glchallenge.dto.PhoneDTO;
import com.gsteren.glchallenge.dto.UserInputDTO;
import com.gsteren.glchallenge.dto.UserResponseDTO;
import com.gsteren.glchallenge.entities.User;
import com.gsteren.glchallenge.repositories.UserRepository;
import com.gsteren.glchallenge.security.JwtUtil;
import com.gsteren.glchallenge.services.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//There are other tests such as using expired tokens etc that are not included
//due to the lack of time.
public class UserControllerIntegrationTest {

	@Autowired
	JwtUtil jwtUtil;
	
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @Autowired
    UserService userService;
    
    private HttpHeaders headers;

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setBearerAuth(jwtUtil.generateToken("test@example.com"));
    }
    
    @Test
    @DirtiesContext
    public void testSignUp_UserAlreadyExists() {
        // Prepare test data
        UserInputDTO userInputDTO = createUserInputDTO();
        userService.createUser(userInputDTO);
        ResponseEntity<ErrorDTO> responseEntity = restTemplate.postForEntity("/api/users/sign-up", userInputDTO, ErrorDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions based on the expected properties of the error response
    }

	private UserInputDTO createUserInputDTO() {
		UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setEmail("john.doe@example.com");
        userInputDTO.setName("Name");
        userInputDTO.setPassword("Abcd1234");
        List<PhoneDTO> phones = new LinkedList<>();
        PhoneDTO phone = new PhoneDTO();
        phone.setCitycode(0);
        phone.setCountrycode("54");
        phone.setNumber(12213);
        phones.add(phone);
        userInputDTO.setPhones(phones);
        
        userInputDTO.setEmail("test@example.com");
		return userInputDTO;
	}
    
    @Test
    @DirtiesContext
    public void testSignUp() {
        // Prepare test data
    	UserInputDTO userInputDTO = createUserInputDTO();
        // Perform the POST request
        ResponseEntity<UserResponseDTO> responseEntity = restTemplate.postForEntity("/api/users/sign-up", userInputDTO, UserResponseDTO.class);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions based on the expected properties of the response

        //Verify the data is saved in the database
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
    }

    @Test
    @DirtiesContext
    public void testLogin() {
    	
    	UserInputDTO userInputDTO = createUserInputDTO();
    	userService.createUser(userInputDTO);
    	
        // Perform the POST request
    	headers = new HttpHeaders();
        headers.setBearerAuth(jwtUtil.generateToken("test@example.com"));
    	ResponseEntity<LoginResponseDTO> responseEntity = restTemplate.postForEntity("/api/users/login", new HttpEntity<>(headers), LoginResponseDTO.class);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    
    @Test
    @DirtiesContext
    public void testLogin_notExistingUserFromToken() {
    	
    	UserInputDTO userInputDTO = createUserInputDTO();
    	userService.createUser(userInputDTO);
    	
        // Perform the POST request
    	headers = new HttpHeaders();
        headers.setBearerAuth(jwtUtil.generateToken("test@examplo.com"));
        ResponseEntity<LoginResponseDTO> responseEntity = null;
        try {
        	responseEntity = restTemplate.postForEntity("/api/users/login", new HttpEntity<>(headers), LoginResponseDTO.class);
        } catch (ResourceAccessException e) {
        	
        }
        // Assertions
        assert(responseEntity == null);
    }
}
