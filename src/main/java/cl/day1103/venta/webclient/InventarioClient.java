package cl.day1103.venta.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class InventarioClient {
    private final WebClient webClient;

    // El constructor lee la propiedad que pusimos en el application.properties
    public InventarioClient(@Value("${inventario-service.url}") String inventarioServidor) {
        this.webClient = WebClient.builder().baseUrl(inventarioServidor).build();
    }

    // Método para enviar mediante PUT la lista de productos y cantidades a descontar
    public void descontarStock(List<Map<String, Object>> productosADescontar) {
        this.webClient.put()
                .uri("/descontar") // Esto llamará a /api/inventario/descontar
                .bodyValue(productosADescontar)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error en Inventario: " + body))))
                .bodyToMono(Void.class) // Usamos Void porque no esperamos datos de vuelta, solo un OK
                .block(); // Lo hacemos síncrono para asegurar que el stock baje antes de terminar la venta
    }
}
