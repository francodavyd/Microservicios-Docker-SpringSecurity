package com.francodavyd.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PedidoDTO {
    private Long id;
    private EEstadoPedido estado;
    private BigDecimal total;
    private List<DetallePedidoDTO> detalles;
}
