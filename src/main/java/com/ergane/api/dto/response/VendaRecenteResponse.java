package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class VendaRecenteResponse {

    private String id;
    private String nomeCliente;
    private LocalDateTime dataHora;
    private BigDecimal valorTotal;

}