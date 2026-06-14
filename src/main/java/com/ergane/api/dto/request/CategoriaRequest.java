package com.ergane.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequest {

    @NotBlank(message = "Nome da categoria é obrigatório.")
    private String nome;

}