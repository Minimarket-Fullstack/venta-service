package cl.day1103.venta.config;

import cl.day1103.venta.model.DetalleVenta;
import cl.day1103.venta.model.Venta;
import cl.day1103.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final VentaRepository ventaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ventaRepository.count() > 0) {
            log.info(">>> DataInitializer: La BD ya tiene datos, se omite la carga inicial");
            return;
        }

        log.info(">>> DataInitializer: BD vacía detectada, insertando historial de ventas de prueba...");


        Venta v1 = new Venta();
        v1.setClienteId(1L);
        v1.setFecha(LocalDate.now().minusDays(1));
        v1.setTotal(new BigDecimal("15000.00"));
        v1.setEstado("COMPLETADA");
        v1.setNumeroVenta("VNT-1");
        v1.setDescuentoTotal(BigDecimal.ZERO);
        v1.setCajero("Miguel");


        List<DetalleVenta> detallesV1 = new ArrayList<>();

        DetalleVenta d1 = new DetalleVenta();
        d1.setProductoId(1L);
        d1.setCantidad(2);
        d1.setPrecioUnitario(new BigDecimal("5000.00"));
        d1.setSubtotal(new BigDecimal("10000.00"));
        d1.setDescuento(BigDecimal.ZERO);
        d1.setVenta(v1);
        detallesV1.add(d1);

        DetalleVenta d2 = new DetalleVenta();
        d2.setProductoId(2L);
        d2.setCantidad(1);
        d2.setPrecioUnitario(new BigDecimal("5000.00"));
        d2.setSubtotal(new BigDecimal("5000.00"));
        d2.setDescuento(BigDecimal.ZERO);
        d2.setVenta(v1);
        detallesV1.add(d2);

        v1.setDetalles(detallesV1);
        ventaRepository.save(v1);


        Venta v2 = new Venta();
        v2.setClienteId(2L);
        v2.setFecha(LocalDate.now());
        v2.setTotal(new BigDecimal("3500.00"));
        v2.setEstado("PENDIENTE");
        v2.setNumeroVenta("VNT-2");
        v2.setDescuentoTotal(BigDecimal.ZERO);
        v2.setCajero("Juan Javier");

        List<DetalleVenta> detallesV2 = new ArrayList<>();

        DetalleVenta d3 = new DetalleVenta();
        d3.setProductoId(3L);
        d3.setCantidad(1);
        d3.setPrecioUnitario(new BigDecimal("3500.00"));
        d3.setSubtotal(new BigDecimal("3500.00"));
        d3.setDescuento(BigDecimal.ZERO);
        d3.setVenta(v2);
        detallesV2.add(d3);

        v2.setDetalles(detallesV2);
        ventaRepository.save(v2);

        log.info(">>> DataInitializer: Carga finalizada. {} documentos de venta insertados correctamente.", ventaRepository.count());
    }
}