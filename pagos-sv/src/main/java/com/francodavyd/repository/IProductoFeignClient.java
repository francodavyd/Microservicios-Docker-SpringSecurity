package com.francodavyd.repository;

import com.francodavyd.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productos-sv")
public interface IProductoFeignClient {
    @GetMapping("product/get/{id}")
    ProductoDTO getProductoById(@PathVariable("id") Long id);
}