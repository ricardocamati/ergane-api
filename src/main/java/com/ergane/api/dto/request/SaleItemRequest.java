package com.ergane.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemRequest {

    @NotBlank(message = "produto_id é obrigatório.")
    private String produtoId;

    @NotBlank(message = "Nome do item é obrigatório.")
    private String nome;

    @NotNull(message = "Quantidade é obrigatória.")
    @Positive(message = "Quantidade deve ser maior que zero.")
    private Integer quantidade;

    @NotNull(message = "Preço unitário é obrigatório.")
    @Positive(message = "Preço unitário deve ser maior que zero.")
    private BigDecimal precoUnitario;

    @NotNull(message = "Preço total é obrigatório.")
    @Positive(message = "Preço total deve ser maior que zero.")
    private BigDecimal precoTotal;

}