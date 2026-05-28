package com.frax.BackEnd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDTO {
    
    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;
    
    @NotBlank(message = "Password obbligatoria")
    private String password;

   
    
}
