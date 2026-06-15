package com.ergane.api.repository;

import com.ergane.api.model.Venda;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends MongoRepository<Venda, String> {

    List<Venda> findTop5ByUsuarioIdOrderByDataHoraDesc(String usuarioId);
    List<Venda> findByUsuarioIdAndDataHoraBetween(String usuarioId, LocalDateTime start, LocalDateTime end);
    Optional<Venda> findByIdAndUsuarioId(String id, String usuarioId);

}