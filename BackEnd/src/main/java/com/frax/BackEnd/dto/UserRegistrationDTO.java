package com.frax.BackEnd.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationDTO {

    @NotBlank(message = "Name è obbligatoria")
    private String name;

    @NotBlank(message = "Surname è obbligatoria")
    private String surname;

    @NotBlank(message = "Conferma la email è obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "Password è obbligatoria")
    @Size(min=6,message="password deve essere almeno di 6 caratteri")
    private String password;

    @Nullable
    private Boolean isAdmin;

    

}