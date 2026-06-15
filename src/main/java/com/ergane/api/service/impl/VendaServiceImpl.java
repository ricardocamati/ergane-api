package com.ergane.api.service.impl;

import com.ergane.api.dto.response.DashboardResponse;
import com.ergane.api.dto.response.VendaRecenteResponse;
import com.ergane.api.dto.request.SaleItemRequest;
import com.ergane.api.dto.request.VendaRequest;
import com.ergane.api.dto.response.VendaResponse;
import com.ergane.api.exception.ApiException;
import com.ergane.api.model.Produto;
import com.ergane.api.model.Venda;
import com.ergane.api.model.ItemVenda;
import com.ergane.api.repository.ProdutoRepository;
import com.ergane.api.repository.VendaRepository;
import com.ergane.api.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements VendaService {

        private final VendaRepository vendaRepository;
        private final ProdutoRepository produtoRepository;

        @Override
        @Transactional
        public VendaResponse create(String userId, VendaRequest request) {
                if (request.getItens() == null || request.getItens().isEmpty()) {
                        throw new ApiException(HttpStatus.BAD_REQUEST,
                                        "Venda não pode ser processada com carrinho vazio.");
                }

                // Agrega as quantidades por produto: tolera o mesmo produto repetido em
                // várias linhas do carrinho (antes isso quebrava com Duplicate key -> 500).
                Map<String, Integer> quantidadePorProduto = new LinkedHashMap<>();
                for (SaleItemRequest item : request.getItens()) {
                        quantidadePorProduto.merge(item.getProdutoId(), item.getQuantidade(), Integer::sum);
                }

                List<ItemVenda> saleItems = new ArrayList<>();
                BigDecimal valorTotal = BigDecimal.ZERO;

                for (Map.Entry<String, Integer> entry : quantidadePorProduto.entrySet()) {
                        String produtoId = entry.getKey();
                        int quantidade = entry.getValue();

                        Produto product = produtoRepository.findByIdAndUsuarioId(produtoId, userId)
                                        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                        "Produto não encontrado: " + produtoId));

                        if (product.getEstoque() == null || product.getEstoque() < quantidade) {
                                throw new ApiException(HttpStatus.BAD_REQUEST,
                                                "Estoque insuficiente para o produto: " + product.getNome());
                        }

                        // O servidor é a fonte da verdade dos preços: nunca confia nos
                        // valores monetários enviados pelo cliente.
                        BigDecimal precoUnitario = product.getPreco();
                        BigDecimal precoTotalItem = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
                        valorTotal = valorTotal.add(precoTotalItem);

                        product.setEstoque(product.getEstoque() - quantidade);
                        produtoRepository.save(product);

                        saleItems.add(ItemVenda.builder()
                                        .produtoId(product.getId())
                                        .nome(product.getNome())
                                        .quantidade(quantidade)
                                        .precoUnitario(precoUnitario)
                                        .precoTotal(precoTotalItem)
                                        .build());
                }

                // Pagamento validado contra o total calculado pelo servidor.
                BigDecimal valorRecebido = request.getValorRecebido();
                if (valorRecebido == null || valorRecebido.compareTo(valorTotal) < 0) {
                        throw new ApiException(HttpStatus.BAD_REQUEST,
                                        "Valor recebido é insuficiente para o total da venda.");
                }
                BigDecimal troco = valorRecebido.subtract(valorTotal);

                Venda sale = Venda.builder()
                                .usuarioId(userId)
                                .nomeCliente(request.getNomeCliente())
                                .cpfCliente(request.getCpfCliente())
                                .metodoPagamento(request.getMetodoPagamento())
                                .valorTotal(valorTotal)
                                .valorRecebido(valorRecebido)
                                .troco(troco)
                                .latitude(request.getLatitude())
                                .longitude(request.getLongitude())
                                .itens(saleItems)
                                .dataHora(LocalDateTime.now())
                                .build();

                Venda saved = vendaRepository.save(sale);

                return VendaResponse.builder()
                                .id(saved.getId())
                                .mensagem("Venda registrada com sucesso e estoque atualizado.")
                                .build();
        }

        @Override
        public DashboardResponse dashboard(String userId) {
                YearMonth month = YearMonth.now();
                LocalDateTime start = month.atDay(1).atStartOfDay();
                LocalDateTime end = month.plusMonths(1).atDay(1).atStartOfDay();

                BigDecimal total = vendaRepository.findByUsuarioIdAndDataHoraBetween(userId, start, end).stream()
                                .map(Venda::getValorTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return DashboardResponse.builder()
                                .totalVendidoMes(total)
                                .assistente(DashboardResponse.Assistente.builder()
                                                .titulo("Thiago:")
                                                .mensagem("Na última quinta, você vendeu mais itens no mês atual.")
                                                .build())
                                .build();
        }

        @Override
        public List<VendaRecenteResponse> recentSales(String userId) {
                return vendaRepository.findTop5ByUsuarioIdOrderByDataHoraDesc(userId).stream()
                                .map(sale -> VendaRecenteResponse.builder()
                                                .id(sale.getId())
                                                .nomeCliente(sale.getNomeCliente())
                                                .dataHora(sale.getDataHora())
                                                .valorTotal(sale.getValorTotal())
                                                .build())
                                .toList();
        }

}