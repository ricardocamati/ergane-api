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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements VendaService {

        private final VendaRepository vendaRepository;
        private final ProdutoRepository produtoRepository;

        @Override
        public VendaResponse create(String userId, VendaRequest request) {
                if (request.getItens() == null || request.getItens().isEmpty()) {
                        throw new ApiException(HttpStatus.BAD_REQUEST,
                                        "Venda não pode ser processada com carrinho vazio.");
                }

                Map<String, SaleItemRequest> requestItemsByProductId = request.getItens().stream()
                                .collect(Collectors.toMap(SaleItemRequest::getProdutoId, item -> item));

                List<Produto> products = request.getItens().stream()
                                .map(item -> produtoRepository.findByIdAndUsuarioId(item.getProdutoId(), userId)
                                                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                                "Produto não encontrado: " + item.getProdutoId())))
                                .toList();

                for (int i = 0; i < products.size(); i++) {
                        Produto product = products.get(i);
                        SaleItemRequest item = requestItemsByProductId.get(product.getId());
                        if (product.getEstoque() == null || product.getEstoque() < item.getQuantidade()) {
                                throw new ApiException(HttpStatus.BAD_REQUEST,
                                                "Estoque insuficiente para o produto: " + product.getNome());
                        }
                }

                List<ItemVenda> saleItems = products.stream()
                                .map(product -> {
                                        SaleItemRequest item = requestItemsByProductId.get(product.getId());
                                        product.setEstoque(product.getEstoque() - item.getQuantidade());
                                        produtoRepository.save(product);

                                        return ItemVenda.builder()
                                                        .produtoId(product.getId())
                                                        .nome(item.getNome())
                                                        .quantidade(item.getQuantidade())
                                                        .precoUnitario(item.getPrecoUnitario())
                                                        .precoTotal(item.getPrecoTotal())
                                                        .build();
                                })
                                .toList();

                Venda sale = Venda.builder()
                                .usuarioId(userId)
                                .nomeCliente(request.getNomeCliente())
                                .cpfCliente(request.getCpfCliente())
                                .metodoPagamento(request.getMetodoPagamento())
                                .valorTotal(request.getValorTotal())
                                .valorRecebido(request.getValorRecebido())
                                .troco(request.getTroco())
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