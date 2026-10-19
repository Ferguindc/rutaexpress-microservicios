package com.rutaexpress.shipments.service;

import com.rutaexpress.shipments.config.RabbitMQConfig;
import com.rutaexpress.shipments.entity.Envio;
import com.rutaexpress.shipments.entity.EstadoEnvio;
import com.rutaexpress.shipments.repository.EnvioRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class EnvioService {

    private final EnvioRepository repository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${catalog.service.url}")
    private String catalogUrl;

    public EnvioService(EnvioRepository repository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Envio> listarTodos() {
        return repository.findAll();
    }

    public Envio obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));
    }

    public Envio crearEnvio(Envio envio) {
        envio.setEstado(EstadoEnvio.CREADO);
        envio.setCodigoSeguimiento("REX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        Envio guardado = repository.save(envio);

        // Publicación de evento asíncrono hacia RabbitMQ
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME, 
                RabbitMQConfig.ROUTING_KEY, 
                guardado
            );
            System.out.println("--> [RabbitMQ] Evento de envío publicado exitosamente: " + guardado.getCodigoSeguimiento());
        } catch (Exception e) {
            System.err.println("--> [RabbitMQ] Error al publicar evento en RabbitMQ: " + e.getMessage());
        }

        return guardado;
    }

    public Envio cambiarEstado(Long id, EstadoEnvio nuevoEstado) {
        Envio envio = obtenerPorId(id);

        // Regla Clave 1: No se puede pasar a EN_RUTA sin haber sido ACEPTADO previamente
        if (nuevoEstado == EstadoEnvio.EN_RUTA &&
                !(envio.getEstado() == EstadoEnvio.ACEPTADO || envio.getEstado() == EstadoEnvio.EN_BODEGA)) {
            throw new RuntimeException("Regla de negocio violada: No se puede cambiar a EN_RUTA sin pasar por ACEPTADO primero.");
        }

        // Regla Clave 2: Al ACEPTAR un envío, se disminuye la capacidad en ms-rutaexpress-catalog
        if (nuevoEstado == EstadoEnvio.ACEPTADO && envio.getEstado() == EstadoEnvio.CREADO) {
            String urlReduceCapacity = catalogUrl + "/" + envio.getServicioId() + "/reduce-capacity?cantidad=1";
            try {
                restTemplate.patchForObject(urlReduceCapacity, null, Object.class);
            } catch (Exception e) {
                throw new RuntimeException("Error al reducir capacidad en Catálogo: " + e.getMessage());
            }
        }

        envio.setEstado(nuevoEstado);
        return repository.save(envio);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Envío no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}
