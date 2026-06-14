package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private UsuarioResumoResponse usuario;

}