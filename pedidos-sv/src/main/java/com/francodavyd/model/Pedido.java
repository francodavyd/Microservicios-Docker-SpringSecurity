package com.francodavyd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usuarioId;
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private EEstadoPedido estado;

    private BigDecimal total;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetallePedido> detalles;
}
