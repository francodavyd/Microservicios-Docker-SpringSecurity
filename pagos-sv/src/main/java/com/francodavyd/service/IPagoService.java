package com.francodavyd.service;

import com.francodavyd.model.Pago;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface IPagoService {
    public String crearPago(Long pedidoId) throws MPException, MPApiException;
    public void actualizarEstadoPago(String paymentId, String status);
}
