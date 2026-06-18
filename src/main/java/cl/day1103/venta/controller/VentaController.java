package cl.day1103.venta.controller;

import cl.day1103.venta.dto.VentaRequestDTO;
import cl.day1103.venta.dto.VentaResponseDTO;
import cl.day1103.venta.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Ventas",
        description = "Operaciones relacionadas con las ventas"
)
@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @Operation(summary = "Buscar una venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> buscarVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    @Operation(summary = "Registrar una nueva venta")
    @PostMapping
    public ResponseEntity<VentaResponseDTO> registrarVenta(
            @Valid @RequestBody VentaRequestDTO dto) {

        VentaResponseDTO nuevaVenta = ventaService.crearVenta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @Operation(summary = "Actualizar el estado de una venta")
    @PutMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> actualizarEstadoVenta(
            @PathVariable Long id,
            @RequestParam String estado) {

        return ResponseEntity.ok(ventaService.cambiarEstado(id, estado));
    }
}