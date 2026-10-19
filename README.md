# RutaExpress - Arquitectura de Microservicios

Este repositorio contiene la implementación y configuración de la arquitectura de microservicios para la plataforma RutaExpress, orientada al procesamiento asíncrono de eventos, gestión de envíos, catálogo y administración de mensajería mediante RabbitMQ.

## Estructura del Proyecto

El sistema está dividido en los siguientes componentes:

* **bff-caso3**: Backend For Frontend / Gateway de entrada encargado de la orquestación y enrutamiento de peticiones.
* **microservicio-shipments**: Gestión de envíos de paquetes y publicación de eventos hacia el broker de mensajería.
* **microservicio-notify**: Consumidor de eventos de notificación con lógica de procesamiento y reintentos.
* **microservicio-admin**: Servicio de administración e integración con RabbitMQ API para la gestión de colas.
* **microservicio-catalog**: Servicio de catálogo para la consulta de productos y servicios disponibles.

## Componentes de Infraestructura

La arquitectura de mensajería asíncrona está basada en **RabbitMQ** bajo las siguientes especificaciones:

* **Flujo Estándar (ACK)**: Procesamiento de eventos en colas principales (`q.cmd.email`).
* **Manejo de Errores (NACK/DLQ)**: Redirección automática hacia colas de letra muerta (`q.cmd.email.dlq`) en caso de fallas de procesamiento o excepciones forzadas.
* **RabbitAdmin**: Monitoreo y creación programática de colas y enlaces (bindings) vía HTTP.

## Configuración y Puertos

Cada microservicio está configurado para ejecutarse en contenedores Docker independientes:

| Servicio | Puerto |
|---|---|
| BFF / Gateway | 8081 |
| Catalog | 9091 |
| Shipments | 9092 |
| Notify | 9093 |
| Admin | 9094 |
| RabbitMQ Management | 15672 |

## Requisitos de Ejecución

* Java 17 / Spring Boot 3.x
* Docker Engine y Docker Compose
* Instancia funcional de RabbitMQ
