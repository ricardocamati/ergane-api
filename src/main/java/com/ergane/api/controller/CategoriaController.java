package com.ergane.api.controller;

import com.ergane.api.dto.request.CategoriaRequest;
import com.ergane.api.dto.response.CategoriaResponse;
import com.ergane.api.security.UserPrincipal;
import com.ergane.api.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(categoriaService.findAll(principal.getUserId()));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(Authentication authentication, @Valid @RequestBody CategoriaRequest request) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.create(principal.getUserId(), request));
    }

}