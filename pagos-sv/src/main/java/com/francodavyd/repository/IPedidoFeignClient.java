package com.francodavyd.repository;

import com.francodavyd.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "pedidos-sv")
public interface IPedidoFeignClient {
    @GetMapping("/pedido/get/{id}")
    public PedidoDTO obtenerPorId(@PathVariable("id") Long id);
    @PutMapping("/pedido/confirm/{id}")
    public void confirmarStock(@PathVariable("id") Long id);
    @PutMapping("/perdido/cancel/{id}")
    public void cancelStock(@PathVariable("id") Long id);

}
