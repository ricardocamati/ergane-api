package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResumoResponse {

    private String id;
    private String nome;

}