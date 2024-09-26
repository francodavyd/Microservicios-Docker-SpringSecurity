package com.francodavyd.service;

import com.francodavyd.model.Producto;
import com.francodavyd.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IProductoServiceImpl implements IProductoService{
    @Autowired
    private IProductoRepository repository;
    @Override
    public void save(Producto producto) {
        repository.save(producto);
    }

    @Override
    public List<Producto> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
     repository.deleteById(id);
    }

    @Override
    public Optional<Producto> update(Long id, Producto producto) {
        Optional<Producto> prod = this.findById(id);
        if (prod.isPresent()) {
            Producto productoExistente = prod.get();

            if (producto.getNombre() != null) {
                productoExistente.setNombre(producto.getNombre());
            }
            if (producto.getDescripcion() != null) {
                productoExistente.setDescripcion(producto.getDescripcion());
            }
            if (producto.getPrecio() != null) {
                productoExistente.setPrecio(producto.getPrecio());
            }
            if (producto.getStockDisponible() != null) {
                productoExistente.setStockDisponible(producto.getStockDisponible());
            }
            if (producto.getCategoria() != null) {
                productoExistente.setCategoria(producto.getCategoria());
            }
            this.save(productoExistente);
            return Optional.of(productoExistente);
        } else {
            throw new RuntimeException("Producto no encontrado con el ID: " + id);
        }
    }


    @Override
    public void updateStock(Long id, int cantidad) {
        Optional<Producto> prod = this.findById(id);
        if (prod.isPresent()){
            prod.get().setStockDisponible(prod.get().getStockDisponible() - cantidad);
            this.save(prod.get());

        } else {
            throw new RuntimeException("Lo sentimos, ha ocurrido un error");
        }
    }

    @Override
    public void reserveStock(Long productoId, int cantidad) {
        Producto producto = repository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStockDisponible() >= cantidad) {
            producto.setStockDisponible(producto.getStockDisponible() - cantidad);
            producto.setStockReservado(producto.getStockReservado() + cantidad);
            repository.save(producto);
        } else {
            throw new RuntimeException("Stock insuficiente para reservar");
        }
    }

    @Override
    public void confirmStock(Long productoId, int cantidad) {
        Producto producto = repository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStockReservado(producto.getStockReservado() - cantidad);
        repository.save(producto);
    }

    @Override
    public void cancelStock(Long productoId, int cantidad) {
        Producto producto = repository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStockReservado(producto.getStockReservado() - cantidad);
        producto.setStockDisponible(producto.getStockDisponible() + cantidad);
        repository.save(producto);
    }
}
