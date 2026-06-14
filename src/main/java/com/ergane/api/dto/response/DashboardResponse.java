package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardResponse {

    private BigDecimal totalVendidoMes;
    private Assistente assistente;

    @Data
    @Builder
    public static class Assistente {
        private String titulo;
        private String mensagem;
    }

}