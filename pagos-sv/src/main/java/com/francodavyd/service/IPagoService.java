package com.francodavyd.service;

import com.francodavyd.model.Pago;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import java.util.List;

public interface IPagoService {
    public String crearPago(Long pedidoId) throws MPException, MPApiException;
    public Pago actualizarEstadoPago(Long paymentId, Long pedido);
    public List<Pago> obtenerLista();
    public Pago obtenerPorId(Long id);
    public void eliminar(Long id);
}
