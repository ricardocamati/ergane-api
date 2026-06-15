package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VendaDetalheResponse {

    private String id;
    private String nomeCliente;
    private String cpfCliente;
    private String metodoPagamento;
    private BigDecimal valorTotal;
    private BigDecimal valorRecebido;
    private BigDecimal troco;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dataHora;
    private List<Item> itens;

    @Data
    @Builder
    public static class Item {
        private String produtoId;
        private String nome;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal precoTotal;
    }

}
