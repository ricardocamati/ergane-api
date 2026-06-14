package com.ergane.api.service.impl;

import com.ergane.api.dto.request.CategoriaRequest;
import com.ergane.api.dto.response.CategoriaResponse;
import com.ergane.api.model.Categoria;
import com.ergane.api.repository.CategoriaRepository;
import com.ergane.api.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoryRepository;

    @Override
    public List<CategoriaResponse> findAll(String userId) {
        return categoryRepository.findAllByUsuarioId(userId).stream()
                .map(cat -> CategoriaResponse.builder()
                        .id(cat.getId())
                        .nome(cat.getNome())
                        .build())
                .toList();
    }

    @Override
    public CategoriaResponse create(String userId, CategoriaRequest request) {
        Categoria category = Categoria.builder()
                .nome(request.getNome())
                .usuarioId(userId)
                .build();

        Categoria saved = categoryRepository.save(category);

        return CategoriaResponse.builder()
                .id(saved.getId())
                .nome(saved.getNome())
                .build();
    }
}
