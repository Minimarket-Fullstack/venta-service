package cl.day1103.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de una venta")
public class VentaResponseDTO {

    @Schema(description = "ID de la venta", example = "1")
    private Long id;

    @Schema(description = "ID del cliente", example = "1")
    private Long clienteId;

    @Schema(description = "Fecha de la venta")
    private LocalDate fecha;

    @Schema(description = "Monto total de la venta", example = "15990")
    private BigDecimal total;

    @Schema(description = "Estado de la venta", example = "PAGADA")
    private String estado;

    @Schema(description = "Número identificador de la venta", example = "VTA-123456")
    private String numeroVenta;

    @Schema(description = "Nombre del cajero")
    private String cajero;

    @Schema(description = "Descuento total aplicado", example = "1000")
    private BigDecimal descuentoTotal;

    @Schema(description = "Detalle de productos vendidos")
    private List<DetalleVentaResponseDTO> detalles;
}