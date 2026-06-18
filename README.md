# venta-service
Microservicio encargado de registrar ventas del minimarket. Valida clientes y productos, calcula el total de la venta, descuenta stock en inventario y permite cambiar el estado de una venta.
## Puerto
```
8082
```
## Tecnologías
- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Eureka Client
- Swagger/OpenAPI
- HATEOAS
- Mockito/JUnit
- Docker
- Railway

## Base de datos
```
db_minimarket
```
## Endpoints V1
```
GET /api/v1/ventas
GET /api/v1/ventas/{id}
POST /api/v1/ventas
PUT /api/v1/ventas/{id}/estado
```
## Endpoints V2 HATEOAS
```
GET /api/v2/ventas
GET /api/v2/ventas/{id}
POST /api/v2/ventas
PUT /api/v2/ventas/{id}/estado
DELETE /api/v2/ventas/{id}
```
## Swagger
```
http://localhost:8082/swagger-ui.html
```
## Ejemplo JSON
```json
{
  "clienteId": 1,
  "cajero": "Daniel",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 1500
    }
  ]
}
```
## Ejecutar pruebas
```bash
mvn test
```
## Ejecutar localmente
```bash
mvn spring-boot:run
```
## Configuración Railway
```properties
server.port=${PORT:8082}
```
Variables recomendadas:

```properties
SPRING_DATASOURCE_URL=jdbc:mysql://HOST:PORT/railway?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME=TU_USUARIO
SPRING_DATASOURCE_PASSWORD=TU_PASSWORD
EUREKA_CLIENT_ENABLED=false
```
