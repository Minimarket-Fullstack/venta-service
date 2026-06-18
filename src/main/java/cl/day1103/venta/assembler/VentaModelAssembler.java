package cl.day1103.venta.assembler;

import cl.day1103.venta.controller.VentaControllerV2;
import cl.day1103.venta.dto.VentaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaResponseDTO, EntityModel<VentaResponseDTO>> {

    @Override
    public EntityModel<VentaResponseDTO> toModel(VentaResponseDTO venta) {

        return EntityModel.of(
                venta,
                linkTo(methodOn(VentaControllerV2.class)
                        .buscarVentaPorId(venta.getId()))
                        .withSelfRel(),

                linkTo(methodOn(VentaControllerV2.class)
                        .listarVentas())
                        .withRel("ventas")
        );
    }
}