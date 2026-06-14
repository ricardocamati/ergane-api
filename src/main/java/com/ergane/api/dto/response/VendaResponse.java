package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendaResponse {

    private String id;
    private String mensagem;

}