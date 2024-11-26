package com.francodavyd.repository;

import com.francodavyd.model.Producto;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@ActiveProfiles("test")
public class IProductoRepositoryTest {
    @Autowired
    private IProductoRepository repository;
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
        Producto prod = repository.save(producto);
        assertThat(prod).isNotNull();
        assertThat(prod.getId()).isNotNull();
        assertThat(prod.getNombre()).isEqualTo("Aceite");
    }
    @DisplayName("Test para obtener lista de productos")
    @Test
    public void getAllTest(){
        repository.save(producto);
        List<Producto> list = repository.findAll();
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }
    @DisplayName("Test para obtener producto por ID")
    @Test
    public void findByIdTest(){
        Producto savedProducto = repository.save(producto);

        Optional<Producto> prod = repository.findById(savedProducto.getId());

        assertThat(prod).isPresent();
        assertThat(prod.get().getNombre()).isEqualTo("Aceite");
    }
    @DisplayName("Test para eliminar un producto")
    @Test
    public void deleteByIdTest(){
        repository.save(producto);
        repository.deleteById(producto.getId());
        Optional<Producto> findAuthor = repository.findById(producto.getId());
        assertThat(findAuthor).isEmpty();
    }
    @DisplayName("Test para actualizar un producto")
    @Test
    public void updateTest(){
        Producto savedProducto = repository.save(producto);

        Optional<Producto> prod = repository.findById(savedProducto.getId());

        assertThat(prod).isPresent();

        prod.get().setNombre("Carne");
        prod.get().setCategoria("Alimentos");
        Producto prodAct = repository.save(prod.get());

        assertThat(prodAct).isNotNull();
        assertThat(prodAct.getId()).isEqualTo(savedProducto.getId());
        assertThat(prodAct.getNombre()).isEqualTo("Carne");
        assertThat(prodAct.getCategoria()).isEqualTo("Alimentos");
    }
}
