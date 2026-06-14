package com.ergane.api.controller;

import com.ergane.api.dto.request.ProdutoRequest;
import com.ergane.api.dto.response.ProdutoResponse;
import com.ergane.api.security.UserPrincipal;
import com.ergane.api.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(produtoService.findAll(principal.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscar(Authentication authentication, @PathVariable String id) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(produtoService.findById(principal.getUserId(), id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(Authentication authentication, @Valid @RequestBody ProdutoRequest request) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.create(principal.getUserId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(Authentication authentication, @PathVariable String id, @Valid @RequestBody ProdutoRequest request) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(produtoService.update(principal.getUserId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(Authentication authentication, @PathVariable String id) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        produtoService.delete(principal.getUserId(), id);

        return ResponseEntity.noContent().build();
    }

}