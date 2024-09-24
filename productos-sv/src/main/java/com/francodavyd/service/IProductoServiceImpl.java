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
            if (producto.getStock() != null) {
                productoExistente.setStock(producto.getStock());
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
    public void actualizarStock(Long id, int cantidad) {
        Optional<Producto> prod = this.findById(id);
        if (prod.isPresent()){
            prod.get().setStock(prod.get().getStock() - cantidad);
            this.save(prod.get());

        } else {
            throw new RuntimeException("Lo sentimos, ha ocurrido un error");
        }
    }
}
