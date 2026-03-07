package com.jiralite.dto;


import com.jiralite.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegister {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotBlank
    @Email
    private String email;
}
