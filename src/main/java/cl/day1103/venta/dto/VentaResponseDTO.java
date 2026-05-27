package cl.day1103.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponseDTO {

    private Long id;
    private Long clienteId;
    private LocalDate fecha;
    private BigDecimal total;
    private String estado;
    private String numeroVenta;
    private String cajero;
    private BigDecimal descuentoTotal;
    private List<DetalleVentaResponseDTO> detalles;
}
