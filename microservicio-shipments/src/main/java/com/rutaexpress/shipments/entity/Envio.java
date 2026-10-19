package com.rutaexpress.shipments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ENVIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoSeguimiento;

    @Column(nullable = false)
    private Long servicioId; // ID del servicio en ms-rutaexpress-catalog

    @Column(nullable = false)
    private String clienteEmail;

    private String direccionOrigen;
    private String direccionDestino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado = EstadoEnvio.CREADO;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
}