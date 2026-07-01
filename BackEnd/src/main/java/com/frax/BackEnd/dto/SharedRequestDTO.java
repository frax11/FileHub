package com.frax.BackEnd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedRequestDTO {

    @NotNull(message = "L'ID del file è obbligatorio")
    private String id;

    @Min(value = 1, message = "Il limite minimo è 1")
    @Max(value = 100, message = "Il limite massimo è 100")
    private int maxAccessCount = 100;

    @NotNull(message = "utente deve essere specificato")
    private String shareTo;
}