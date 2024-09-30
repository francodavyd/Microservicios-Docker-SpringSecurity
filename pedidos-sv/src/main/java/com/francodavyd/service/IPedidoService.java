package com.francodavyd.service;

import com.francodavyd.model.EEstadoPedido;
import com.francodavyd.model.Pedido;

import java.util.Optional;

public interface IPedidoService {
    public void save(Pedido pedido);
    public Optional<Pedido> findById(Long id);
    public Pedido updateOrderStatus(Long pedidoId, EEstadoPedido estadoPedido);
    public void confirmStock(Long idProducto);
    public void cancelStock(Long idProducto);
}
