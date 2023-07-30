package com.gsteren.glchallenge.dto;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class UserInputDTO {
	@NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d{2})[a-zA-Z\\d]{8,12}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, and two digits")
    private String password;

    @NotEmpty
    private List<@Valid PhoneDTO> phones;

    // Getters and setters

    // Constructors
}
