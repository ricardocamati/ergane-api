package com.ergane.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProdutoRequest {

    @NotBlank(message = "Nome do produto é obrigatório.")
    private String nome;

    @NotNull(message = "Preço é obrigatório.")
    @PositiveOrZero(message = "Preço deve ser maior ou igual a zero.")
    private BigDecimal preco;

    @NotNull(message = "Custo de produção é obrigatório.")
    @PositiveOrZero(message = "Custo de produção deve ser maior ou igual a zero.")
    private BigDecimal custoProducao;

    @NotNull(message = "Estoque é obrigatório.")
    @PositiveOrZero(message = "Estoque deve ser maior ou igual a zero.")
    private Integer estoque;

    @NotEmpty(message = "Informe ao menos uma categoria.")
    private List<@NotBlank(message = "Categoria não pode ser vazia.") String> categorias;

    @NotBlank(message = "Descrição é obrigatória.")
    private String descricao;

}