package com.francodavyd.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.francodavyd.model.Producto;
import com.francodavyd.service.IProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "PRIVATE_KEY=fakePrivateKey",
        "PUBLIC_KEY=fakePublicKey",
        "GENERATOR=fakeGenerator"
})
public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IProductoService service;
    @Autowired
    private ObjectMapper mapper;
    private Producto producto;
    @BeforeEach
    public void setUp() {
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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void saveTest() throws Exception {
        given(service.save(any(Producto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(producto)));

        mockMvc.perform(post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Aceite\",\"descripcion\":\"Aceite de girasol 1L\",\"precio\":1600,\"stockDisponible\":10,\"stockReservado\":0,\"categoria\":\"Comida\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Producto creado correctamente"));
    }

    @DisplayName("Test para obtener lista de productos")
    @Test
    public void getAllTest() throws Exception {
        List<Producto> list = List.of(producto);
        given(service.getAll()).willReturn(list);
        ResultActions response = mockMvc.perform(get("/product/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(list)));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(list.size())));
    }
    @DisplayName("Test para obtener un producto por id")
    @Test
    public void findByIdTest() throws Exception {
        given(service.findById(producto.getId())).willReturn(Optional.of(producto));
        ResultActions response = mockMvc.perform(get("/product/get/{id}",producto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(producto))
        );
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre",is(producto.getNombre())));
    }
    @DisplayName("Test para eliminar un producto por id")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteByIdTest() throws Exception {
        willDoNothing().given(service).deleteById(producto.getId());
        ResultActions response = mockMvc.perform(delete("/product/delete/{id}",producto.getId())
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());

    }
    @DisplayName("Test para editar un producto")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateTest() throws Exception {
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Aceite de oliva");
        productoActualizado.setDescripcion("Aceite negro 1L");
        productoActualizado.setPrecio(new BigDecimal(1690));
        productoActualizado.setStockDisponible(10);
        productoActualizado.setStockReservado(0);
        productoActualizado.setCategoria("Comida");

        given(service.findById(producto.getId())).willReturn(Optional.of(producto));
        given(service.update(any(Long.class), any(Producto.class))).willReturn(Optional.of(productoActualizado));
        ResultActions response = mockMvc.perform(put("/product/update/{id}", producto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productoActualizado)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre",is(productoActualizado.getNombre())))
                .andExpect(jsonPath("$.descripcion",is(productoActualizado.getDescripcion())));;

    }

    @DisplayName("Test para actualizar stock de un producto")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateStockTest() throws Exception {
        given(service.findById(producto.getId())).willReturn(Optional.of(producto));
        given(service.save(producto)).willReturn(producto);

        ResultActions response = mockMvc.perform(put("/product/stock/{id}/{cantidad}", producto.getId(), 5)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());

    }
    @DisplayName("Test para reservar stock de un producto")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void reserveStockTest() throws Exception{
        given(service.findById(producto.getId())).willReturn(Optional.of(producto));
        given(service.save(producto)).willReturn(producto);

        ResultActions response = mockMvc.perform(put("/product/reservar/{id}/{cantidad}", producto.getId(), 5)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
    }
    @DisplayName("Test para confirmar stock de un producto")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void confirmStockTest() throws Exception {
        given(service.findById(producto.getId())).willReturn(Optional.of(producto));
        given(service.save(producto)).willReturn(producto);

        ResultActions response = mockMvc.perform(put("/product/confirmar/{id}/{cantidad}", producto.getId(), 5)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
    }
    @DisplayName("Test para cancelar stock de un producto")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void cancelStockTest() throws Exception {
        given(service.findById(producto.getId())).willReturn(Optional.of(producto));
        given(service.save(producto)).willReturn(producto);

        ResultActions response = mockMvc.perform(put("/product/cancelar/{id}/{cantidad}", producto.getId(), 5)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
    }
}
