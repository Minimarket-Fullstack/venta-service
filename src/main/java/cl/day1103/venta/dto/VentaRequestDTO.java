package cl.day1103.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para registrar una venta")
public class VentaRequestDTO {

    @Schema(description = "ID del cliente", example = "1")
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @Schema(description = "Nombre del cajero", example = "Juan Pérez")
    @NotBlank(message = "El nombre del cajero es obligatorio")
    private String cajero;

    @Schema(description = "Lista de productos vendidos")
    @NotEmpty(message = "La venta debe contener al menos un producto")
    @Valid
    private List<DetalleVentaRequestDTO> detalles;
}