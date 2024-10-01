package com.francodavyd.repository;

import com.francodavyd.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "pedidos-sv" , url = "http://localhost:8090/pedido")
public interface IPedidoFeignClient {
    @GetMapping("/get/{id}")
    public PedidoDTO obtenerPorId(@PathVariable("id") Long id);
    @PutMapping("/confirm/{id}")
    public void confirmarStock(@PathVariable("id") Long id);
    @PutMapping("/cancel/{id}")
    public void cancelStock(@PathVariable("id") Long id);

}
