package com.pedidos360.catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SERVICIOS_ENVIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // Ej: "Express 24h", "Estándar Mismo Día", "Carga Pesada"

    private String descripcion;

    @Column(nullable = false)
    private Double tarifa; // Costo base del envío

    @Column(nullable = false)
    private Integer capacidadDisponible; // Regla: disminuye al aceptar un envío

    private Boolean activo = true;
}