package com.francodavyd.service;

import com.francodavyd.model.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    public void save(Producto producto);
    public List<Producto> getAll();
    public Optional<Producto> findById(Long id);
    public void deleteById(Long id);
    public Optional<Producto> update(Long id, Producto producto);
    public void actualizarStock(Long id, int cantidad);
}
