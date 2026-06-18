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

        void findById_deberiaRetornarVenta() {
            Venta venta = new Venta(1L, 1L, LocalDate.now(), BigDecimal.valueOf(15990), "PENDIENTE", "VNT-1", BigDecimal.ZERO, "Daniel", null);
            when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

            VentaResponseDTO resultado = ventaService.findById(1L);

            assertEquals(1L, resultado.getId());
            assertEquals("PENDIENTE", resultado.getEstado());
            verify(ventaRepository).findById(1L);
        }

        @Test
        void cambiarEstado_deberiaActualizarEstado() {        verify(ventaRepository).findAll();
    }

    @Test
        Venta venta = new Venta(1L, 1L, LocalDate.now(), BigDecimal.valueOf(15990), "PENDIENTE", "VNT-1", BigDecimal.ZERO, "Daniel", null);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        VentaResponseDTO resultado = ventaService.cambiarEstado(1L, "PAGADA");

        assertEquals("PAGADA", resultado.getEstado());
        verify(ventaRepository).save(any(Venta.class));
    }
}
