package com.pedidos360.catalog.controller;

import com.pedidos360.catalog.entity.ServicioEnvio;
import com.pedidos360.catalog.service.ServicioEnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/services")
@CrossOrigin(origins = "*")
public class ServicioEnvioController {

    private final ServicioEnvioService service;

    public ServicioEnvioController(ServicioEnvioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServicioEnvio>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioEnvio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServicioEnvio> crear(@RequestBody ServicioEnvio servicio) {
        return ResponseEntity.ok(service.guardar(servicio));
    }

    // Endpoint expuesto para que ms-rutaexpress-shipments descuente capacidad
    @PatchMapping("/{id}/reduce-capacity")
    public ResponseEntity<ServicioEnvio> reducirCapacidad(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer cantidad) {
        return ResponseEntity.ok(service.reducirCapacidad(id, cantidad));
    }
}