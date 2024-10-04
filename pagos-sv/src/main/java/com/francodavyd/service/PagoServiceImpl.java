package com.francodavyd.service;

import com.francodavyd.dto.DetallePedidoDTO;
import com.francodavyd.dto.EEstadoPedido;
import com.francodavyd.dto.PedidoDTO;
import com.francodavyd.dto.ProductoDTO;
import com.francodavyd.model.EEstadoPago;
import com.francodavyd.model.Pago;
import com.francodavyd.repository.IPagoRepository;
import com.francodavyd.repository.IPedidoFeignClient;
import com.francodavyd.repository.IProductoFeignClient;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service

public class PagoServiceImpl implements IPagoService {
    @Autowired
    private IPagoRepository repository;
    @Autowired
    private IPedidoFeignClient client;
    @Autowired
    private IProductoFeignClient clientP;
    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Override
    @Transactional
    public String crearPago(Long pedidoId) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accessToken);

        PedidoDTO pedido = client.obtenerPorId(pedidoId);

        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
        }

        List<PreferenceItemRequest> items = new ArrayList<>();

        for (DetallePedidoDTO detalle : pedido.getDetalles()) {
            ProductoDTO producto = clientP.getProductoById(detalle.getProductoId());

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(String.valueOf(producto.getId()))
                    .title(producto.getNombre())
                    .quantity(detalle.getCantidad())
                    .unitPrice(detalle.getPrecioUnitario())
                    .build();
            items.add(itemRequest);
        }

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        if (preference == null || preference.getId() == null) {
            throw new RuntimeException("Error al crear la preferencia de pago");
        }

        repository.save(Pago.builder()
                .pedidoId(pedidoId)
                .total(pedido.getTotal())
                .status(EEstadoPago.PENDIENTE)
                .preferenceId(preference.getId())
                .urlPago(preference.getSandboxInitPoint()) // En producción cambiar por getInitPoint()
                .fechaCreacion(LocalDate.now())
                .build());

        return preference.getSandboxInitPoint();
    }

    @Override
    @Transactional
    public Pago actualizarEstadoPago(Long idPago, Long pedidoId) {
        Pago pago = this.obtenerPorId(idPago);
        if (pago != null){
            pago.setStatus(EEstadoPago.PAGADO);
            client.confirmarStock(pedidoId);
            return pago;
        } else {
            throw new RuntimeException("Error al confirmar pago");
        }
    }

    @Override
    public List<Pago> obtenerLista() {
        return repository.findAll();
    }

    @Override
    public Pago obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
