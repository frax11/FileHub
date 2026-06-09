package com.frax.BackEnd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class SharedRequestDTO {

    @NotNull(message = "L'ID del file è obbligatorio")
    private String Id;

    @Min(value = 1, message = "Il limite minimo è 1")
    @Max(value = 100, message = "Il limite massimo è 100")
    private int maxAccessCount = 100;

    @NotNull(message = "Almeno un utente deve essere specificato")
    private List<String> userEmails;  // email degli utenti con cui condividere
}