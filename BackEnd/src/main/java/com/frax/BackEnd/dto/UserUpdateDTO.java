package com.frax.BackEnd.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    @NotBlank(message = "Name è obbligatoria")
    private String name;

    @NotBlank(message = "Surname è obbligatoria")
    private String surname;

    @Nullable
    @Size(min = 6, message = "password deve essere almeno di 6 caratteri")
    private String updatePassword;


    @Size(min = 6, message = "password deve essere almeno di 6 caratteri")
    private String currentPassword;


}
