package com.pedidos360.catalog.repository;

import com.pedidos360.catalog.entity.ServicioEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioEnvioRepository extends JpaRepository<ServicioEnvio, Long> {
}