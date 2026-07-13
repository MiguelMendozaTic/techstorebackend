package com.techstore.techstore_api.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://techstorefrontend-six.vercel.app")
public class DemoController {

    @GetMapping("/hola")
    public String hola() {
        return "Hola usuario autenticado";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Solo ADMIN";
    }
}