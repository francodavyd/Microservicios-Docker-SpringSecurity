package com.francodavyd.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private EEstadoPago status;
    private String preferenceId;
    private String urlPago;
    private String paymentId;
    private LocalDate fechaCreacion;
}
