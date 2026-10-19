package com.pedidos360.catalog.service;

import com.pedidos360.catalog.entity.ServicioEnvio;
import com.pedidos360.catalog.repository.ServicioEnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioEnvioService {

    private final ServicioEnvioRepository repository;

    public ServicioEnvioService(ServicioEnvioRepository repository) {
        this.repository = repository;
    }

    public List<ServicioEnvio> listarTodos() {
        return repository.findAll();
    }

    public ServicioEnvio obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio de envío no encontrado con ID: " + id));
    }

    public ServicioEnvio guardar(ServicioEnvio servicio) {
        return repository.save(servicio);
    }

    // Regla de Negocio: Reducir capacidad disponible al aceptar un envío
    public ServicioEnvio reducirCapacidad(Long id, Integer cantidad) {
        ServicioEnvio servicio = obtenerPorId(id);

        if (servicio.getCapacidadDisponible() < cantidad) {
            throw new RuntimeException("Capacidad insuficiente para el servicio seleccionando");
        }

        servicio.setCapacidadDisponible(servicio.getCapacidadDisponible() - cantidad);
        return repository.save(servicio);
    }
}