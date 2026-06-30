package com.frax.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class UserDTO {

    private String id;
    private String name;
    private String surname;
    private String email;
    private boolean isAdmin;

   
    
}
