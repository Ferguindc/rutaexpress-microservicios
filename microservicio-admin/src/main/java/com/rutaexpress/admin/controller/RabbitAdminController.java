package com.rutaexpress.admin.controller;

import com.rutaexpress.admin.service.RabbitAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rabbitmq")
public class RabbitAdminController {

    private final RabbitAdminService adminService;

    public RabbitAdminController(RabbitAdminService adminService) {
        this.adminService = adminService;
    }

    // --- CREAR COLA ---
    @PostMapping("/queues")
    public ResponseEntity<String> createQueue(@RequestParam(required = false) String queueName) {
        if (queueName == null || queueName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El parámetro 'queueName' es obligatorio");
        }
        adminService.createQueue(queueName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cola '" + queueName + "' creada exitosamente");
    }

    // --- ELIMINAR COLA ---
    @DeleteMapping("/queues/{queueName}")
    public ResponseEntity<String> deleteQueue(@PathVariable String queueName) {
        if (queueName == null || queueName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El parámetro 'queueName' es obligatorio");
        }
        boolean deleted = adminService.deleteQueue(queueName);
        return deleted ? ResponseEntity.ok("Cola '" + queueName + "' eliminada")
                       : ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cola no existía");
    }

    // --- CREAR EXCHANGE ---
    @PostMapping("/exchanges")
    public ResponseEntity<String> createExchange(@RequestParam(required = false) String exchangeName,
                                                 @RequestParam(defaultValue = "direct") String type) {
        if (exchangeName == null || exchangeName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El parámetro 'exchangeName' es obligatorio");
        }
        adminService.createExchange(exchangeName, type);
        return ResponseEntity.status(HttpStatus.CREATED).body("Exchange '" + exchangeName + "' tipo (" + type + ") creado");
    }

    // --- ELIMINAR EXCHANGE ---
    @DeleteMapping("/exchanges/{exchangeName}")
    public ResponseEntity<String> deleteExchange(@PathVariable String exchangeName) {
        if (exchangeName == null || exchangeName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El parámetro 'exchangeName' es obligatorio");
        }
        boolean deleted = adminService.deleteExchange(exchangeName);
        return deleted ? ResponseEntity.ok("Exchange '" + exchangeName + "' eliminado")
                       : ResponseEntity.status(HttpStatus.NOT_FOUND).body("El exchange no existía");
    }

    // --- CREAR BINDING ---
    @PostMapping("/bindings")
    public ResponseEntity<String> createBinding(@RequestParam(required = false) String queueName,
                                                @RequestParam(required = false) String exchangeName,
                                                @RequestParam(required = false) String routingKey) {
        if (queueName == null || queueName.isBlank() ||
            exchangeName == null || exchangeName.isBlank() ||
            routingKey == null || routingKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Los parámetros 'queueName', 'exchangeName' y 'routingKey' son todos obligatorios");
        }
        adminService.createBinding(queueName, exchangeName, routingKey);
        return ResponseEntity.status(HttpStatus.CREATED).body("Binding creado correctamente");
    }

    // --- ELIMINAR BINDING ---
    @DeleteMapping("/bindings")
    public ResponseEntity<String> deleteBinding(@RequestParam(required = false) String queueName,
                                                @RequestParam(required = false) String exchangeName,
                                                @RequestParam(required = false) String routingKey) {
        if (queueName == null || queueName.isBlank() ||
            exchangeName == null || exchangeName.isBlank() ||
            routingKey == null || routingKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Los parámetros 'queueName', 'exchangeName' y 'routingKey' son todos obligatorios");
        }
        adminService.deleteBinding(queueName, exchangeName, routingKey);
        return ResponseEntity.ok("Binding eliminado correctamente");
    }
}
