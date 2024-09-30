package com.francodavyd.controller;

import com.francodavyd.service.IPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pago")
public class PagoController {
    @Autowired
    private IPagoService service;

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
    public ResponseEntity<?> obtenerLista(){
        try {
            return new ResponseEntity<>(service.obtenerLista(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
}
