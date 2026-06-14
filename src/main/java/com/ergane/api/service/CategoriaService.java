package com.ergane.api.service;

import java.util.List;

import com.ergane.api.dto.request.CategoriaRequest;
import com.ergane.api.dto.response.CategoriaResponse;

public interface CategoriaService {

    List<CategoriaResponse> findAll(String userId);
    CategoriaResponse create(String userId, CategoriaRequest request);

}