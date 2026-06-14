package com.ergane.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "Senha é obrigatória.")
    private String senha;

}