package com.rutaexpress.shipments.repository;

import com.rutaexpress.shipments.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByClienteEmail(String clienteEmail);
}