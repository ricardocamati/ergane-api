package com.ergane.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProdutoResponse {

    private String id;
    private String nome;
    private BigDecimal preco;
    private BigDecimal custoProducao;
    private Integer estoque;
    private List<String> categorias;
    private String descricao;

}