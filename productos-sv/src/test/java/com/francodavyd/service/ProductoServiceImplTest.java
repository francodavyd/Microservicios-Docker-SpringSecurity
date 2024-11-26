package com.francodavyd.service;

import com.francodavyd.model.Producto;
import com.francodavyd.repository.IProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {
    @Mock
    private IProductoRepository repository;
    @InjectMocks
    private ProductoServiceImpl service;
    private Producto producto;
    @BeforeEach
    public void setUp(){
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aceite");
        producto.setDescripcion("Aceite de girasol 1L");
        producto.setPrecio(new BigDecimal(1600));
        producto.setStockDisponible(10);
        producto.setStockReservado(0);
        producto.setCategoria("Comida");
    }
    @DisplayName("Test para guardar un producto")
    @Test
    public void saveTest(){
        given(repository.save(producto)).willReturn(producto);
        Producto prod = service.save(producto);
        assertThat(prod).isNotNull();
        assertThat(prod.getNombre()).isEqualTo("Aceite");
    }
    @DisplayName("Test para obtener lista de productos")
    @Test
    public void getAllTest(){
        given(repository.findAll()).willReturn(List.of(producto));
        List<Producto> list = service.getAll();
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }
    @DisplayName("Test para obtener producto por ID")
    @Test
    public void findByIdTest(){
        given(repository.findById(producto.getId())).willReturn(Optional.of(producto));
        Producto prod = service.findById(producto.getId()).get();

        assertThat(prod).isNotNull();
    }
    @DisplayName("Test para eliminar un producto")
    @Test
    public void deleteByIdTest(){
        willDoNothing().given(repository).deleteById(producto.getId());

        service.deleteById(producto.getId());

        verify(repository,times(1)).deleteById(producto.getId());
    }
    @DisplayName("Test para editar un producto")
    @Test
    public void updateTest(){
        given(repository.findById(producto.getId())).willReturn(Optional.of(producto));
        given(repository.save(producto)).willReturn(producto);

        producto.setNombre("Carne asada");
        Optional<Producto> prod = service.update(producto.getId(), producto);


        assertThat(prod).isPresent();
        assertThat(prod.get().getNombre()).isEqualTo("Carne asada");

    }
}
