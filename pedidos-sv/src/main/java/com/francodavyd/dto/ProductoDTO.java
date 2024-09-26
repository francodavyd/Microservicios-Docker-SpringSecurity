package com.francodavyd.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductoDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stockDisponible;
    private Integer stockReservado;

}
