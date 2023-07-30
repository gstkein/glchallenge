package com.gsteren.glchallenge.dto;
import java.util.List;

import lombok.Data;

@Data
public class LoginResponseDTO extends UserResponseDTO {

    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;

    // Getters and setters
    // Constructors
}
