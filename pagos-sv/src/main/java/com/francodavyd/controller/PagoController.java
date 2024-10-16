package com.francodavyd.controller;

import com.francodavyd.repository.IPagoRepository;
import com.francodavyd.service.IPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pago")
@PreAuthorize("permitAll()")
public class PagoController {
    @Autowired
    private IPagoService service;
    @Autowired
    private IPagoRepository repository;
    @Value("${mercadopago.webhook.token}")
    private String hookToken;

    @PostMapping("/save/{pedidoId}")
    public ResponseEntity<?> crearPago(@PathVariable("pedidoId") Long pedidoId){
        try {
            String url = service.crearPago(pedidoId);
            return new ResponseEntity<>("Solicitud de pago creada correctamente, ingresa al siguiente link para pagar: " + url, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("Error al crear solicitud de pago, intente nuevamente: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> obtenerLista(){
        try {
            return new ResponseEntity<>(service.obtenerLista(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }

}
