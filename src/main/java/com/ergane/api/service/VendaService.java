package com.ergane.api.service;

import java.util.List;

import com.ergane.api.dto.response.DashboardResponse;
import com.ergane.api.dto.request.VendaRequest;
import com.ergane.api.dto.response.VendaDetalheResponse;
import com.ergane.api.dto.response.VendaRecenteResponse;
import com.ergane.api.dto.response.VendaResponse;

public interface VendaService {

    VendaResponse create(String userId, VendaRequest request);
    DashboardResponse dashboard(String userId);
    List<VendaRecenteResponse> recentSales(String userId);
    VendaDetalheResponse detail(String userId, String id);

}