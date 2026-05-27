package cl.day1103.venta.controller;
import cl.day1103.venta.dto.VentaRequestDTO;
import cl.day1103.venta.dto.VentaResponseDTO;
import cl.day1103.venta.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    //LISTAR TODAS LAS VENTAS
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    //BUSCAR UNA VENTA POR SU ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> buscarVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO dto) {

        VentaResponseDTO nuevaVenta = ventaService.crearVenta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> actualizarEstadoVenta(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(ventaService.cambiarEstado(id, estado));
    }
}