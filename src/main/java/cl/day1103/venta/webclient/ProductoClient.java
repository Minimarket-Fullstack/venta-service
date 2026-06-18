package cl.day1103.venta.webclient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ProductoClient {
    private final WebClient webClient;

    public ProductoClient(@Value("${producto-service.url}") String productoServidor){
        this.webClient = WebClient.builder().baseUrl(productoServidor).build();
    }

    public Map<String, Object> obtenerProductoId(Long id){
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("El producto con ID " + id + " no existe."))))
                .bodyToMono(Map.class)
                .block();
    }
}