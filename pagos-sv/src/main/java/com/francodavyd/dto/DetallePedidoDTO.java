package com.francodavyd.dto;

import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class DetallePedidoDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
