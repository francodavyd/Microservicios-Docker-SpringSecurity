package com.francodavyd.controller;

import com.francodavyd.model.EEstadoPedido;
import com.francodavyd.model.Pedido;
import com.francodavyd.repository.IPagoFeignClient;
import com.francodavyd.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
@PreAuthorize("permitAll()")
public class PedidoController {
    @Autowired
    private IPedidoService service;

    @PostMapping("/save")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido){
    try {
        Pedido pedidoGuardado = service.save(pedido);
        String url = service.createPayment(pedidoGuardado.getId());
        return new ResponseEntity<>( url, HttpStatus.CREATED);
    } catch (Exception e){
        e.printStackTrace();
        return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente: " + e.getMessage() + e.getCause(), HttpStatus.BAD_REQUEST);
    }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id){
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("El pedido solicitado no se ha encontrado", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateStatus/{idPedido}/{estadoPedido}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long idPedido, @PathVariable EEstadoPedido estadoPedido){
        try {
            return new ResponseEntity<>(service.updateOrderStatus(idPedido, estadoPedido), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/confirm/{id}")
    public ResponseEntity<?> confirmarStock(@PathVariable Long id){
        try {
            service.confirmStock(id);
            return new ResponseEntity<>("El stock del producto solicitado ha sido modificado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelStock(@PathVariable Long id){
        try {
            service.cancelStock(id);
            return new ResponseEntity<>("El stock del producto solicitado ha sido liberado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
