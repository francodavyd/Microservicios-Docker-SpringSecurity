package com.francodavyd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.francodavyd.model.Pago;
import com.francodavyd.repository.IPagoRepository;
import com.francodavyd.repository.IPedidoFeignClient;
import com.francodavyd.service.IPagoService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/webhook")
public class WebHookController {

    @Autowired
    private IPagoRepository repository;
    @Autowired
    private IPagoService service;

    @Value("${mercadopago.webhook.token}")
    private String hookToken;
    @Value("${mercadopago.access.token}")
    private String authToken;
    @Autowired
    private IPedidoFeignClient client;

    @PostMapping
    public ResponseEntity<?> handleWebhook(HttpServletRequest request,
                                           @RequestHeader(value = "x-signature", required = false) String signature,
                                           @RequestHeader(value = "x-request-id", required = false) String requestId) throws IOException {

        // Leer el cuerpo crudo de la solicitud
        String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);

        // Validar la firma
        if (signature != null && requestId != null && !validateSignature(signature, payload, requestId, hookToken)) {
            return ResponseEntity.status(403).body("Firma inválida");
        }

        // Convertir el payload a Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payloadMap = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

        // Extraer el paymentId del payload
        Map<String, Object> data = (Map<String, Object>) payloadMap.get("data");
        String paymentId = (String) data.get("id");
        System.out.println("Payment ID obtenido del payload: " + paymentId);


        //Prueba para la simulacion que ofrece Mercado Pago, luego se implementaran pruebas unitarias
        if ("100".equals(paymentId)) {
            System.out.println("Simulación de pago recibida con ID: " + paymentId);


            return ResponseEntity.ok("Simulación de pago manejada correctamente");
        }

        // Consultar el pago en Mercado Pago
        try {
            Payment payment = getPaymentDetailsFromMercadoPago(Long.parseLong(paymentId));

            // Buscar el pago en la base de datos usando el preferenceId
            Optional<Pago> pagoOpt = repository.findByPreferenceId(String.valueOf(payment.getOrder().getId()));

            if (pagoOpt.isPresent()) {
                Pago pago = pagoOpt.get();
                // Procesar el estado del pago
                if ("approved".equals(payment.getStatus())) {
                    Pago pagoP = service.actualizarEstadoPago(pago.getId(), pago.getPedidoId());
                    pago.setPaymentId(paymentId);
                    repository.save(pago);
                    System.out.println("Pago aprobado y registrado correctamente");
                } else {
                    System.out.println("El pago no fue aprobado. Estado: " + payment.getStatus());
                    client.cancelStock(pago.getPedidoId());
                    service.eliminar(pago.getId());
                }
            } else {
                System.out.println("No se encontró un pago con el preferenceId correspondiente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al procesar el pago");
        }

        return ResponseEntity.ok().build();
    }

    // Nueva lógica de validación de firma basada en el template de Mercado Pago
    private boolean validateSignature(String signature, String payload, String requestId, String secret) {
        try {
            // Extraer ts y v1 del header x-signature
            String[] signatureParts = signature.split(",");
            String tsValue = signatureParts[0].split("=")[1];
            String v1Value = signatureParts[1].split("=")[1];

            // Extraer el id del payload
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> data = (Map<String, Object>) payloadMap.get("data");
            String id = (String) data.get("id");

            // Crear el template de firma
            String signedTemplate = "id:" + id + ";request-id:" + requestId + ";ts:" + tsValue + ";";

            // Generar la contrafirma usando HMAC-SHA256
            String computedSignature = new HmacUtils("HmacSHA256", secret).hmacHex(signedTemplate);

            // Comparar la firma recibida (v1) con la generada
            return computedSignature.equals(v1Value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Payment getPaymentDetailsFromMercadoPago(Long paymentId) throws MPException, MPApiException {
        try {
            MercadoPagoConfig.setAccessToken(authToken);
            PaymentClient paymentClient = new PaymentClient();
            return paymentClient.get(paymentId);
        } catch (MPApiException e) {
            System.out.println("Error de API: Código de estado - " + e.getApiResponse().getStatusCode());
            System.out.println("Respuesta del cuerpo: " + e.getApiResponse().getContent());
            throw e;  // re-lanza la excepción para que el controlador también capture esto si es necesario
        } catch (Exception e) {
            System.out.println("Error inesperado al obtener detalles del pago: " + e.getMessage());
            throw e;
        }
    }
}