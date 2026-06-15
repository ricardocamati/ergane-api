package com.ergane.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vendas")
public class Venda {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String usuarioId;
    private String nomeCliente;
    private String cpfCliente;
    private String metodoPagamento;
    private BigDecimal valorTotal;
    private BigDecimal valorRecebido;
    private BigDecimal troco;
    private Double latitude;
    private Double longitude;

    private List<ItemVenda> itens;

    private LocalDateTime dataHora;
}