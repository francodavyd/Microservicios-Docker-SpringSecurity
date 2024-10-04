package com.francodavyd.service;

import com.francodavyd.dto.ProductoDTO;
import com.francodavyd.model.DetallePedido;
import com.francodavyd.model.EEstadoPedido;
import com.francodavyd.model.Pedido;
import com.francodavyd.repository.IPedidoRepository;
import com.francodavyd.repository.IProductoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements IPedidoService{
    @Autowired
    private IPedidoRepository repository;

    @Autowired
    private IProductoFeignClient iProductoFeignClient;
    @Override
    @Transactional
    public void save(Pedido pedido) {
        // Verificar disponibilidad de productos en el catálogo
        for (DetallePedido detalle : pedido.getDetalles()) {
            ProductoDTO producto = iProductoFeignClient.getProductoById(detalle.getProductoId());

            // Verificar si hay suficiente stock disponible
            if (producto.getStockDisponible() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Reservar el stock
            iProductoFeignClient.reservStock(detalle.getProductoId(), detalle.getCantidad());

            // Actualizar el precio del detalle con el precio actual del producto
            detalle.setPrecioUnitario(producto.getPrecio());
        }

        // Calcular el total del pedido
        BigDecimal total = pedido.getDetalles().stream()
                .map(detalle -> detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setTotal(total);
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(EEstadoPedido.PENDIENTE); // Asignar el estado inicial

        // Guardar el pedido en la base de datos
        repository.save(pedido);
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Pedido updateOrderStatus(Long pedidoId, EEstadoPedido estadoPedido) {
        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estadoPedido);

        return repository.save(pedido);

    }


    @Override
    public void confirmStock(Long idProducto) {
        Pedido pedido = repository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Lógica para liberar el stock reservado
        for (DetallePedido detalle : pedido.getDetalles()) {
            iProductoFeignClient.confirmStock(detalle.getProductoId(), detalle.getCantidad());
        }

        // Actualizar el estado del pedido a CONFIRMADO
        pedido.setEstado(EEstadoPedido.PAGADO);
        repository.save(pedido);
    }

    @Override
    public void cancelStock(Long idProducto) {
        Pedido pedido = repository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Lógica para liberar el stock reservado
        for (DetallePedido detalle : pedido.getDetalles()) {
            iProductoFeignClient.cancelStock(detalle.getProductoId(), detalle.getCantidad());
        }

        // Actualizar el estado del pedido a CANCELADO
        pedido.setEstado(EEstadoPedido.CANCELADO);
        repository.save(pedido);
    }


}
