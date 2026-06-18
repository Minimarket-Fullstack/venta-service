# venta-service

Microservicio encargado de registrar ventas del minimarket. Valida cliente y productos, calcula el total de la venta, descuenta stock desde `inventario-service` y puede enviar alertas a `notificaciones-service` si ocurre un error en la venta.

## Puerto

```text
8082
```

## Tecnologías

- Java 21
- Spring Boot 3.3.5
- Spring Data JPA
- MySQL
- WebClient
- Eureka Client
- Swagger/OpenAPI
- HATEOAS
- Mockito/JUnit
- Docker

## Base de datos

```text
db_minimarket
```

## Endpoints V1

```text
GET  /api/v1/ventas
GET  /api/v1/ventas/{id}
POST /api/v1/ventas
PUT  /api/v1/ventas/{id}/estado
```

## Endpoints V2 HATEOAS

```text
GET    /api/v2/ventas
GET    /api/v2/ventas/{id}
POST   /api/v2/ventas
PUT    /api/v2/ventas/{id}/estado?estado=PAGADA
DELETE /api/v2/ventas/{id}
```

## Swagger

```text
http://localhost:8082/swagger-ui.html
```

## Ejemplo JSON para crear venta

```json
{
  "clienteId": 1,
  "cajero": "Daniel",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```

## Ejecutar pruebas

```bash
mvn test
```
