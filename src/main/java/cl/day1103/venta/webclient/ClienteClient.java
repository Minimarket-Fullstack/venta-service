package cl.day1103.venta.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component // Para que Spring Boot lo detecte automáticamente
public class ClienteClient {
    private final WebClient webClient;

    // El constructor recibe la URL de clientes desde el application.properties
    public ClienteClient(@Value("${cliente-service.url}") String clienteServidor){
        this.webClient = WebClient.builder().baseUrl(clienteServidor).build();
    }

    // Método para comunicarse con cliente-service y verificar por ID
    public Map<String, Object> obtenerClienteId(Long id){
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("El cliente con ID " + id + " no existe."))))
                .bodyToMono(Map.class)
                .block();
    }
}
