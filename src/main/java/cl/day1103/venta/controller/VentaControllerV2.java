package cl.day1103.venta.controller;

import cl.day1103.venta.assembler.VentaModelAssembler;
import cl.day1103.venta.dto.VentaRequestDTO;
import cl.day1103.venta.dto.VentaResponseDTO;
import cl.day1103.venta.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas V2 - HATEOAS", description = "CRUD de ventas con enlaces HATEOAS")
public class VentaControllerV2 {

    private final VentaService ventaService;
    private final VentaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar ventas con HATEOAS")
    public CollectionModel<EntityModel<VentaResponseDTO>> listarVentas() {
        var ventas = ventaService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaControllerV2.class).listarVentas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar venta por ID con HATEOAS")
    public EntityModel<VentaResponseDTO> buscarVentaPorId(@PathVariable Long id) {
        return assembler.toModel(ventaService.findById(id));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear venta con HATEOAS")
    public ResponseEntity<EntityModel<VentaResponseDTO>> crearVenta(@Valid @RequestBody VentaRequestDTO dto) {
        VentaResponseDTO venta = ventaService.crearVenta(dto);
        return ResponseEntity.status(201).body(assembler.toModel(venta));
    }

    @PutMapping(value = "/{id}/estado", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar estado de venta con HATEOAS")
    public EntityModel<VentaResponseDTO> cambiarEstado(@PathVariable Long id,
                                                       @RequestParam String estado) {
        return assembler.toModel(ventaService.cambiarEstado(id, estado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar venta")
    public ResponseEntity<Map<String, String>> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.ok(Map.of("mensaje", "Venta eliminada correctamente"));
    }
}
