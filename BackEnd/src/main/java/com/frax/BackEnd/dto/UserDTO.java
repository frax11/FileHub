package com.frax.BackEnd.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String surname;
    private String email;
    private Boolean isAdmin;
   
    public UserDTO(String name, String surname, String email, Boolean isAdmin) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isAdmin = isAdmin;
    }
}
