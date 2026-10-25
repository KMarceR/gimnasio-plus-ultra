package com.sv.grupo7.gimnasio_plus_ultra.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Direccion donde corre el frontend con Vite
    private static final String ORIGEN_FRONTEND = "http://localhost:5173";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")                                    // solo las rutas de la API
                .allowedOrigins(ORIGEN_FRONTEND)                          // solo el frontend, no cualquier sitio
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS para la preflight
                .allowedHeaders("*")                                       // permite Content-Type: application/json
                .maxAge(3600);                                             // el navegador recuerda el permiso 1 hora
    }
}