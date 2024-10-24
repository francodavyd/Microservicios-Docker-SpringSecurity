package com.francodavyd.repository;

import com.francodavyd.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "productos-sv")
public interface IProductoFeignClient {
    @GetMapping("/product/get/{id}")
    ProductoDTO getProductoById(@PathVariable("id") Long id);
    @PutMapping("/product/reservar/{id}/{cantidad}")
    void reservStock(@PathVariable("id") Long productoId, @PathVariable("cantidad") Integer cantidad);
    @PutMapping("/product/confirmar/{id}/{cantidad}")
    void confirmStock(@PathVariable("id") Long productoId, @PathVariable("cantidad") Integer cantidad);
    @PutMapping("/product/cancelar/{id}/{cantidad}")
    void cancelStock(@PathVariable("id") Long productoId, @PathVariable("cantidad") Integer cantidad);
}
