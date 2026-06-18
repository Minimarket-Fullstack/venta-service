package cl.day1103.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle de una venta registrada")
public class DetalleVentaResponseDTO {

    @Schema(description = "ID del detalle", example = "1")
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Schema(description = "Cantidad vendida", example = "3")
    private Integer cantidad;

    @Schema(description = "Precio unitario", example = "4990")
    private BigDecimal precioUnitario;

    @Schema(description = "Subtotal", example = "14970")
    private BigDecimal subtotal;

    @Schema(description = "Descuento aplicado", example = "1000")
    private BigDecimal descuento;
}