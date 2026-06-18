import cl.day1103.venta.dto.VentaResponseDTO;
import cl.day1103.venta.model.Venta;
import cl.day1103.venta.repository.VentaRepository;
import cl.day1103.venta.service.VentaService;
import cl.day1103.venta.webclient.ClienteClient;
import cl.day1103.venta.webclient.InventarioClient;
import cl.day1103.venta.webclient.NotificacionClient;
import cl.day1103.venta.webclient.ProductoClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import cl.day1103.venta.dto.DetalleVentaRequestDTO;
import cl.day1103.venta.dto.VentaRequestDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceMockitoTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private ProductoClient productoClient;
    @Mock
    private ClienteClient clienteClient;
    @Mock
    private InventarioClient inventarioClient;
    @Mock
    private NotificacionClient notificacionClient;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void findAll_deberiaRetornarVentas() {
        Venta venta = new Venta(1L, 1L, LocalDate.now(), BigDecimal.valueOf(15990), "PENDIENTE", "VNT-1", BigDecimal.ZERO, "Daniel", null);
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<VentaResponseDTO> resultado = ventaService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("VNT-1", resultado.get(0).getNumeroVenta());
        verify(ventaRepository).findAll();
    }

    @Test
    void findById_deberiaRetornarVenta() {
        Venta venta = new Venta(1L, 1L, LocalDate.now(), BigDecimal.valueOf(15990), "PENDIENTE", "VNT-1", BigDecimal.ZERO, "Daniel", null);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        VentaResponseDTO resultado = ventaService.findById(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(ventaRepository).findById(1L);
    }

    @Test
    void cambiarEstado_deberiaActualizarEstado() {
        Venta venta = new Venta(1L, 1L, LocalDate.now(), BigDecimal.valueOf(15990), "PENDIENTE", "VNT-1", BigDecimal.ZERO, "Daniel", null);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        VentaResponseDTO resultado = ventaService.cambiarEstado(1L, "PAGADA");

        assertEquals("PAGADA", resultado.getEstado());
        verify(ventaRepository).save(any(Venta.class));
    }

@Test
    void findById_deberiaLanzarExcepcionSiVentaNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ventaService.findById(99L));
        verify(ventaRepository).findById(99L);
    }

    @Test
    void cambiarEstado_deberiaLanzarExcepcionSiVentaNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ventaService.cambiarEstado(99L, "PAGADA"));
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void eliminarVenta_deberiaEliminarVentaExistente() {
        Venta venta = new Venta(1L, 1L, LocalDate.now(), BigDecimal.valueOf(15990), "PENDIENTE", "VNT-1", BigDecimal.ZERO, "Daniel", null);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        ventaService.eliminarVenta(1L);

        verify(ventaRepository).delete(venta);
    }

    @Test
    void eliminarVenta_deberiaLanzarExcepcionSiNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ventaService.eliminarVenta(99L));
        verify(ventaRepository, never()).delete(any(Venta.class));
    }

    @Test
    void crearVenta_deberiaGuardarVentaYDescontarStock() {
        DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO(1L, 2, BigDecimal.valueOf(1000));
        VentaRequestDTO dto = new VentaRequestDTO(1L, "Daniel", List.of(detalle));
        when(clienteClient.obtenerClienteId(1L)).thenReturn(java.util.Map.of("id", 1L));
        when(productoClient.obtenerProductoId(1L)).thenReturn(java.util.Map.of("id", 1L));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta venta = inv.getArgument(0);
            venta.setId(1L);
            return venta;
        });

        VentaResponseDTO resultado = ventaService.crearVenta(dto);

        assertEquals(1L, resultado.getId());
        assertEquals("Daniel", resultado.getCajero());
        verify(inventarioClient).descontarStock(anyList());
        verify(ventaRepository).save(any(Venta.class));
    }
}
