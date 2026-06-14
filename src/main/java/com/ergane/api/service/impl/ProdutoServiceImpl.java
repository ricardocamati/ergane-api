package com.ergane.api.service.impl;

import com.ergane.api.dto.request.ProdutoRequest;
import com.ergane.api.dto.response.ProdutoResponse;
import com.ergane.api.exception.ApiException;
import com.ergane.api.model.Produto;
import com.ergane.api.repository.ProdutoRepository;
import com.ergane.api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository productRepository;

    @Override
    public List<ProdutoResponse> findAll(String userId) {
        return productRepository.findAllByUsuarioId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProdutoResponse findById(String userId, String id) {
        Produto product = productRepository.findByIdAndUsuarioId(id, userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
        return toResponse(product);
    }

    @Override
    public ProdutoResponse create(String userId, ProdutoRequest request) {
        Produto product = Produto.builder()
                .nome(request.getNome())
                .preco(request.getPreco())
                .custoProducao(request.getCustoProducao())
                .estoque(request.getEstoque())
                .categorias(request.getCategorias())
                .descricao(request.getDescricao())
                .usuarioId(userId)
                .build();

        return toResponse(productRepository.save(product));
    }

    @Override
    public ProdutoResponse update(String userId, String id, ProdutoRequest request) {
        Produto product = productRepository.findByIdAndUsuarioId(id, userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        product.setNome(request.getNome());
        product.setPreco(request.getPreco());
        product.setCustoProducao(request.getCustoProducao());
        product.setEstoque(request.getEstoque());
        product.setCategorias(request.getCategorias());
        product.setDescricao(request.getDescricao());

        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(String userId, String id) {
        Produto product = productRepository.findByIdAndUsuarioId(id, userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        if (product.getEstoque() != null && product.getEstoque() > 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Não é permitido excluir produto com estoque maior que 0.");
        }

        productRepository.deleteByIdAndUsuarioId(id, userId);
    }

    private ProdutoResponse toResponse(Produto product) {
        return ProdutoResponse.builder()
                .id(product.getId())
                .nome(product.getNome())
                .preco(product.getPreco())
                .custoProducao(product.getCustoProducao())
                .estoque(product.getEstoque())
                .categorias(product.getCategorias())
                .descricao(product.getDescricao())
                .build();
    }

}