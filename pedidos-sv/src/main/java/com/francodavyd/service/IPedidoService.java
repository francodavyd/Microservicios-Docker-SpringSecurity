package com.francodavyd.service;

import com.francodavyd.model.EEstadoPedido;
import com.francodavyd.model.Pedido;

import java.util.Optional;

public interface IPedidoService {
    public Pedido save(Pedido pedido);
    public Optional<Pedido> findById(Long id);
    public Pedido updateOrderStatus(Long pedidoId, EEstadoPedido estadoPedido);
    public void confirmStock(Long idProducto, Integer cantidad);
    public void cancelStock(Long idProducto, Integer cantidad);
}
