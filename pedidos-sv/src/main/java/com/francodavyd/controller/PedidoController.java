package com.francodavyd.controller;

import com.francodavyd.model.EEstadoPedido;
import com.francodavyd.model.Pedido;
import com.francodavyd.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    private IPedidoService service;
    @PostMapping("/save")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido){
    try {
        service.save(pedido);
        return new ResponseEntity<>("Pedido creado correctamente", HttpStatus.CREATED);
    } catch (Exception e){
        return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.BAD_REQUEST);
    }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id){
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("El producto solicitado no se ha encontrado", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/updateStatus/{idPedido/{estado}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long idPedido, @PathVariable EEstadoPedido estadoPedido){
        try {
            return new ResponseEntity<>(service.updateOrderStatus(idPedido, estadoPedido), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/confirm/{idProducto}/{cantidad}")
    public ResponseEntity<?> confirmarStock(@PathVariable Long idProducto, @PathVariable int cantidad){
        try {
            service.confirmStock(idProducto, cantidad);
            return new ResponseEntity<>("El stock del producto solicitado ha sido modificado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/cancel/{idProducto}/{cantidad}")
    public ResponseEntity<?> cancelStock(@PathVariable Long idProducto, @PathVariable int cantidad){
        try {
            service.cancelStock(idProducto, cantidad);
            return new ResponseEntity<>("El stock del producto solicitado ha sido liberado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos ha ocurrido un error, intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
}
