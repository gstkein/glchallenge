package com.gsteren.glchallenge.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gsteren.glchallenge.dto.LoginResponseDTO;
import com.gsteren.glchallenge.dto.PhoneDTO;
import com.gsteren.glchallenge.dto.UserInputDTO;
import com.gsteren.glchallenge.dto.UserResponseDTO;
import com.gsteren.glchallenge.entities.Phone;
import com.gsteren.glchallenge.entities.User;
import com.gsteren.glchallenge.exception.CustomException;
import com.gsteren.glchallenge.exception.InputDataException;
import com.gsteren.glchallenge.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserInputDTO userInput) {
        Optional<User> existingUser = userRepository.findByEmail(userInput.getEmail());
        if (existingUser.isPresent()) {
            throw new InputDataException("User with email already exists");
        }

        String encryptedPassword = passwordEncoder.encode(userInput.getPassword());

        // Map UserInputDTO to User entity
        User user = new User();
        user.setName(userInput.getName());
        user.setEmail(userInput.getEmail());
        user.setPassword(encryptedPassword);

        // Set creation and login dates
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        List<Phone> phones = userInput.getPhones().stream().map(this::mapPhoneDTOToPhones).collect(Collectors.toList());
        phones.stream().forEach(p -> p.setUser(user));
        user.setPhones(phones);
        User createdUser = userRepository.save(user);
        
  ;
        // Set other fields as required
       

        // Map User entity to UserResponseDTO
        UserResponseDTO userResponse = mapUserToUserDTO(createdUser);

        return userResponse;
    }
    
    public LoginResponseDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User with email" + email + " does not exists"));

        // Map User entity to UserResponseDTO
        LoginResponseDTO userResponse = mapUserToUserLoginDTO(user);

        return userResponse;
    }

	private UserResponseDTO mapUserToUserDTO(User createdUser) {
		UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(createdUser.getId());
        userResponse.setCreated(createdUser.getCreated());
        userResponse.setLastLogin(createdUser.getLastLogin());
        userResponse.setActive(createdUser.isActive());
		return userResponse;
	}
	
	private LoginResponseDTO mapUserToUserLoginDTO(User user) {
		LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setCreated(user.getCreated());
        response.setLastLogin(user.getLastLogin());
        response.setActive(user.isActive());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword()); //This can not be decoded and should not for security reasons. The encoding method could be changed so it can be reversed but it is not advisable.
        List<PhoneDTO> phonesDto = user.getPhones().stream().map(this::mapPhoneToPhoneDTO).collect(Collectors.toList());
        response.setPhones(phonesDto);
		return response;
	}


	private Phone mapPhoneDTOToPhones(PhoneDTO dto) {
		Phone phone = new Phone();
		phone.setCitycode(dto.getCitycode());
		phone.setCountrycode(dto.getCountrycode());
		phone.setNumber(dto.getNumber());
		return phone;
	}
	
	private PhoneDTO mapPhoneToPhoneDTO(Phone phone) {
		PhoneDTO dto = new PhoneDTO();
		dto.setCitycode(phone.getCitycode());
		dto.setCountrycode(phone.getCountrycode());
		dto.setNumber(dto.getNumber());
		return dto;
	}
}
