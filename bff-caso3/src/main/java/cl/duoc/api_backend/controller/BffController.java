package cl.duoc.api_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class BffController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${microservicio.shipments.url}")
    private String shipmentsUrl;

    @Value("${microservicio.catalog.url}")
    private String catalogUrl;

    // Helper para peticiones GET y DELETE (Sin Body)
    private HttpEntity<Void> createAuthHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }
        return new HttpEntity<>(headers);
    }

    // Helper para peticiones POST, PUT y PATCH (Con Body)
    private <T> HttpEntity<T> createAuthEntity(HttpServletRequest request, T body) {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    // =========================================================
    // 1. MS-RUTAEXPRESSSHIPMENTS (/api/shipments)
    // =========================================================

    // GET: Obtener envíos (soporta filtro opcional ?email=)
// GET: Obtener envíos (Soporta filtrado por ?email= o ?clienteEmail=)
    @GetMapping("/shipments")
    public ResponseEntity<?> getShipments(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String clienteEmail,
            HttpServletRequest request
    ) {
        try {
            HttpEntity<Void> entity = createAuthHeader(request);

            // Determina cuál parámetro se usó en la consulta
            String filtroEmail = (email != null && !email.isEmpty()) ? email : clienteEmail;
            String targetUrl = shipmentsUrl + "/api/shipments";

            // Si viene un correo, reenvía tanto ?email= como ?clienteEmail= al microservicio
            if (filtroEmail != null && !filtroEmail.isEmpty()) {
                targetUrl += "?email=" + filtroEmail + "&clienteEmail=" + filtroEmail;
            }

            ResponseEntity<Object> response = restTemplate.exchange(
                    targetUrl,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Error al conectar con ms-shipments: " + e.getMessage());
        }
    }

    // POST: Crear nuevo envío (RESUELVE EL ERROR 405)
    @PostMapping("/shipments")
    public ResponseEntity<?> createShipment(@RequestBody Object body, HttpServletRequest request) {
        try {
            HttpEntity<Object> entity = createAuthEntity(request, body);
            ResponseEntity<Object> response = restTemplate.exchange(
                    shipmentsUrl + "/api/shipments",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Error al crear envío en ms-shipments: " + e.getMessage());
        }
    }

    // PATCH: Actualizar estado del envío
    @PatchMapping("/shipments/{id}/status")
    public ResponseEntity<?> updateShipmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpServletRequest request
    ) {
        try {
            HttpEntity<Void> entity = createAuthHeader(request);
            String targetUrl = shipmentsUrl + "/api/shipments/" + id + "/status?status=" + status;

            ResponseEntity<Object> response = restTemplate.exchange(
                    targetUrl,
                    HttpMethod.PATCH,
                    entity,
                    Object.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Error al actualizar estado en ms-shipments: " + e.getMessage());
        }
    }

    // =========================================================
    // 2. MS-RUTAEXPRESSCATALOG (/api/catalog/services)
    // =========================================================

    // GET: Obtener lista del catálogo
    @GetMapping("/catalog/services")
    public ResponseEntity<?> getCatalogServices(HttpServletRequest request) {
        try {
            HttpEntity<Void> entity = createAuthHeader(request);
            ResponseEntity<Object> response = restTemplate.exchange(
                    catalogUrl + "/api/catalog/services",
                    HttpMethod.GET,
                    entity,
                    Object.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Error al conectar con ms-catalog: " + e.getMessage());
        }
    }

    // POST: Crear nuevo tipo de servicio en el catálogo
    @PostMapping("/catalog/services")
    public ResponseEntity<?> createCatalogService(@RequestBody Object body, HttpServletRequest request) {
        try {
            HttpEntity<Object> entity = createAuthEntity(request, body);
            ResponseEntity<Object> response = restTemplate.exchange(
                    catalogUrl + "/api/catalog/services",
                    HttpMethod.POST,
                    entity,
                    Object.class
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Error al crear servicio en ms-catalog: " + e.getMessage());
        }
    }

    // DELETE: Eliminar un pedido por ID
    @DeleteMapping("/shipments/{id}")
    public ResponseEntity<?> deleteShipment(@PathVariable Long id, HttpServletRequest request) {
        try {
            HttpEntity<Void> entity = createAuthHeader(request);
            ResponseEntity<Object> response = restTemplate.exchange(
                    shipmentsUrl + "/api/shipments/" + id,
                    HttpMethod.DELETE,
                    entity,
                    Object.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Error al eliminar envío en ms-shipments: " + e.getMessage());
        }
    }
}