package com.francodavyd.repository;

import com.francodavyd.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "productos-sv", url = "http://localhost:8085/product")
public interface IProductoFeignClient {
    @GetMapping("/get/{id}")
    ProductoDTO getProductoById(@PathVariable("id") Long id);
    @PutMapping("/reservar/{id}/{cantidad}")
    void reservStock(@PathVariable("id") Long productoId, @PathVariable("cantidad") Integer cantidad);
    @PutMapping("/confirmar/{id}/{cantidad}")
    void confirmStock(@PathVariable("id") Long productoId, @PathVariable("cantidad") Integer cantidad);
    @PutMapping("/cancelar/{id}/{cantidad}")
    void cancelStock(@PathVariable("id") Long productoId, @PathVariable("cantidad") Integer cantidad);
}
