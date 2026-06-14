package com.ergane.api.service;

import java.util.List;

import com.ergane.api.dto.request.ProdutoRequest;
import com.ergane.api.dto.response.ProdutoResponse;

public interface ProdutoService {

    List<ProdutoResponse> findAll(String userId);
    ProdutoResponse findById(String userId, String id);
    ProdutoResponse create(String userId, ProdutoRequest request);
    ProdutoResponse update(String userId, String id, ProdutoRequest request);
    void delete(String userId, String id);

}