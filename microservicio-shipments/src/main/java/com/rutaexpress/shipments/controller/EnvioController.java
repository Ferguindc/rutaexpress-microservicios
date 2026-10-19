package com.rutaexpress.shipments.controller;

import com.rutaexpress.shipments.entity.Envio;
import com.rutaexpress.shipments.entity.EstadoEnvio;
import com.rutaexpress.shipments.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "*")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Envio>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Envio> crear(@RequestBody Envio envio) {
        return ResponseEntity.ok(service.crearEnvio(envio));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Envio> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoEnvio status) {
        return ResponseEntity.ok(service.cambiarEstado(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}