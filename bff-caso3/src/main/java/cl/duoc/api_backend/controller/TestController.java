package cl.duoc.api_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @GetMapping("/api/public")

    public String publicEndpoint() {

        return "API funcionando correctamente";

    }

}
