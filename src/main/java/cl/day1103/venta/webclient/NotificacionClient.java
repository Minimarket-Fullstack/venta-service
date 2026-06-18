package cl.day1103.venta.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class NotificacionClient {

    private final WebClient webClient;

    public NotificacionClient(@Value("${notificaciones-service.url}") String urlNotificaciones) {
        // Invocamos directamente al builder estático nativo de WebFlux
        this.webClient = WebClient.builder().baseUrl(urlNotificaciones).build();
    }

    public Map<String, Object> enviarAlerta(Map<String, Object> datosAlerta) {
        return this.webClient.post()
                .uri("")
                .bodyValue(datosAlerta)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error en Notificaciones: " + body)))
                )
                .bodyToMono(Map.class)
                .block();
    }
}