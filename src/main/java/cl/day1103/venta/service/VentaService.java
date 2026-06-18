package cl.day1103.venta.service;

import cl.day1103.venta.dto.DetalleVentaResponseDTO;
import cl.day1103.venta.dto.VentaRequestDTO;
import cl.day1103.venta.dto.VentaResponseDTO;
import cl.day1103.venta.model.DetalleVenta;
import cl.day1103.venta.model.Venta;
import cl.day1103.venta.repository.VentaRepository;
import cl.day1103.venta.webclient.ClienteClient;
import cl.day1103.venta.webclient.InventarioClient;
import cl.day1103.venta.webclient.NotificacionClient;
import cl.day1103.venta.webclient.ProductoClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private NotificacionClient notificacionClient;

    public List<VentaResponseDTO> findAll() {
        log.info("[VENTAS] Solicitando listado histórico de todas las ventas.");
        return ventaRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public VentaResponseDTO findById(Long id) {
        log.info("[VENTAS] Buscando boleta con ID: {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[VENTAS ERROR] No se encontró la venta con ID: {}", id);
                    return new RuntimeException("VENTA NO ENCONTRADA");
                });
        return convertToResponseDto(venta);
    }

    @Transactional
    public VentaResponseDTO crearVenta(VentaRequestDTO dto) {
        log.info("[VENTAS INFO]  Iniciando flujo de venta. Cliente ID: {}, Cajero: {}", dto.getClienteId(), dto.getCajero());


        log.info("[VENTAS INFO]  Validando existencia del cliente...");
        clienteClient.obtenerClienteId(dto.getClienteId());
        log.info("[VENTAS SUCCESS]  Cliente verificado con éxito.");


        Venta venta = new Venta();
        venta.setClienteId(dto.getClienteId());
        venta.setCajero(dto.getCajero());
        venta.setEstado("PENDIENTE");
        venta.setFecha(LocalDate.now());
        venta.setDescuentoTotal(BigDecimal.ZERO);


        long cantidadVentas = ventaRepository.count();
        venta.setNumeroVenta("VNT-" + (cantidadVentas + 1));
        log.info("[VENTAS INFO] Generando número de documento: {}", venta.getNumeroVenta());

        BigDecimal total = BigDecimal.ZERO;


        log.info("[VENTAS INFO] ️ Procesando ítems de la venta y consultando precios en maestro de Productos...");
        List<DetalleVenta> detalles = dto.getDetalles().stream().map(detalleVentaRequestDTO -> {
            DetalleVenta d = new DetalleVenta();
            d.setProductoId(detalleVentaRequestDTO.getProductoId());
            d.setCantidad(detalleVentaRequestDTO.getCantidad());
            d.setDescuento(BigDecimal.ZERO);


            Map<String, Object> productoReal = productoClient.obtenerProductoId(detalleVentaRequestDTO.getProductoId());

            Number precioNum = (Number) productoReal.get("precio");
            BigDecimal precioProducto = new BigDecimal(precioNum.toString());

            d.setPrecioUnitario(precioProducto);

            BigDecimal subtotal = precioProducto.multiply(BigDecimal.valueOf(detalleVentaRequestDTO.getCantidad()));
            d.setSubtotal(subtotal);
            d.setVenta(venta);

            log.info("[VENTAS DETALLE] -> Producto ID: {} | Cantidad: {} | Precio Ref: ${} | Subtotal: ${}",
                    d.getProductoId(), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());

            return d;
        }).collect(Collectors.toList());


        for (DetalleVenta d : detalles) {
            total = total.add(d.getSubtotal());
        }

        venta.setDetalles(detalles);
        venta.setTotal(total);
        log.info("[VENTAS INFO]  Monto total calculado para la venta: ${}", venta.getTotal());


        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("[VENTAS INFO]  Registro base guardado localmente.");

        List<Map<String, Object>> productosAEnviar = ventaGuardada.getDetalles().stream().map(d -> {
            Map<String, Object> mapaProducto = new java.util.HashMap<>();
            mapaProducto.put("productoId", d.getProductoId());
            mapaProducto.put("cantidad", d.getCantidad());
            return mapaProducto;
        }).collect(Collectors.toList());

        log.info("[VENTAS INFO]  Despachando orden de descuento sincrónica a Inventario...");
        try {
            inventarioClient.descontarStock(productosAEnviar);
            log.info("[VENTAS SUCCESS]  Inventario procesó los descuentos correctamente. Transacción finalizada con éxito.");
        } catch (RuntimeException e) {
            log.error("[VENTAS ERROR]  Operación cancelada: Inventario rechazó la transacción. Motivo: {}", e.getMessage());


            try {
                log.info("[VENTAS -> NOTIFICACIONES] Enviando alerta de error de venta automáticamente...");


                Map<String, Object> datosAlerta = Map.of(
                        "tipo", "VENTA_FALLIDA",
                        "mensaje", "No se pudo concretar el documento " + ventaGuardada.getNumeroVenta() + ". Motivo: " + e.getMessage()
                );


                notificacionClient.enviarAlerta(datosAlerta);
                log.info("[VENTAS -> NOTIFICACIONES] Alerta registrada exitosamente en el historial.");
            } catch (Exception ex) {

                log.error("[VENTAS ERROR] Falló el intento de comunicación con el servicio de Notificaciones: {}", ex.getMessage());
            }


            throw e;
        }

        return convertToResponseDto(ventaGuardada);
    }

    public VentaResponseDTO cambiarEstado(Long id, String estado) {
        log.info("[VENTAS] Solicitud de cambio de estado para venta ID: {}. Nuevo estado: {}", id, estado);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[VENTAS ERROR] No se pudo cambiar el estado. ID {} no existe.", id);
                    return new RuntimeException("VENTA NO ENCONTRADA");
                });
        venta.setEstado(estado);
        return convertToResponseDto(ventaRepository.save(venta));
    }


    public void eliminarVenta(Long id) {
        log.info("[VENTAS] Solicitud para eliminar venta ID: {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VENTA NO ENCONTRADA"));
        ventaRepository.delete(venta);
    }

    private VentaResponseDTO convertToResponseDto(Venta venta) {
        VentaResponseDTO res = new VentaResponseDTO();
        res.setId(venta.getId());
        res.setClienteId(venta.getClienteId());
        res.setFecha(venta.getFecha());
        res.setTotal(venta.getTotal());
        res.setEstado(venta.getEstado());
        res.setNumeroVenta(venta.getNumeroVenta());
        res.setCajero(venta.getCajero());
        res.setDescuentoTotal(venta.getDescuentoTotal());

        if (venta.getDetalles() != null) {
            res.setDetalles(venta.getDetalles().stream().map(d -> {
                DetalleVentaResponseDTO dRes = new DetalleVentaResponseDTO();
                dRes.setId(d.getId());
                dRes.setProductoId(d.getProductoId());
                dRes.setCantidad(d.getCantidad());
                dRes.setPrecioUnitario(d.getPrecioUnitario());
                dRes.setSubtotal(d.getSubtotal());
                dRes.setDescuento(d.getDescuento());
                return dRes;
            }).collect(Collectors.toList()));
        }
        return res;
    }
}