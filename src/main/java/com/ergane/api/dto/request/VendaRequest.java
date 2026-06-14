package com.ergane.api.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VendaRequest {

    @NotBlank(message = "Nome do cliente é obrigatório.")
    private String nomeCliente;

    @NotBlank(message = "CPF do cliente é obrigatório.")
    private String cpfCliente;

    @NotBlank(message = "Método de pagamento é obrigatório.")
    private String metodoPagamento;

    @NotNull(message = "Valor total é obrigatório.")
    private BigDecimal valorTotal;

    @NotNull(message = "Valor recebido é obrigatório.")
    private BigDecimal valorRecebido;

    @NotNull(message = "Troco é obrigatório.")
    private BigDecimal troco;

    private Double latitude;
    private Double longitude;

    @NotEmpty(message = "Carrinho não pode estar vazio.")
    @Valid
    private List<SaleItemRequest> itens;

}