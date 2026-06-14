package com.ergane.api.controller;

import com.ergane.api.dto.response.DashboardResponse;
import com.ergane.api.dto.response.VendaRecenteResponse;
import com.ergane.api.dto.request.VendaRequest;
import com.ergane.api.dto.response.VendaResponse;
import com.ergane.api.security.UserPrincipal;
import com.ergane.api.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(vendaService.dashboard(principal.getUserId()));
    }

    @GetMapping("/vendas/recentes")
    public ResponseEntity<List<VendaRecenteResponse>> recentes(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(vendaService.recentSales(principal.getUserId()));
    }

    @PostMapping("/vendas")
    public ResponseEntity<VendaResponse> criar(Authentication authentication, @Valid @RequestBody VendaRequest request) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.create(principal.getUserId(), request));
    }

}