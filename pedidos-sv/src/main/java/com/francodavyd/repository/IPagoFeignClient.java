package com.francodavyd.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pagos-sv")
public interface IPagoFeignClient {
    @PostMapping("/pago/save/{pedidoId}")
    public String crearPago(@PathVariable("pedidoId") Long pedidoId);

}
