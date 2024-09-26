package com.francodavyd.controller;

import com.francodavyd.model.Producto;
import com.francodavyd.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductoController {
    @Autowired
    private IProductoService service;
    @PostMapping("/save")
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto){
        try {
            service.save(producto);
            return new ResponseEntity<>("Producto creado correctamente", HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> obtenerLista(){
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id){
      try {
          return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
      } catch (Exception e){
          return new ResponseEntity<>("El producto solicitado no se ha encontrado", HttpStatus.NOT_FOUND);
      }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id){
        try {
            service.deleteById(id);
            return new ResponseEntity<>("Producto eliminado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> editarProducto(@PathVariable Long id, @RequestBody Producto producto){
        try {
            return new ResponseEntity<>(service.update(id, producto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/stock/{id}/{cantidad}")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @PathVariable int cantidad){
        try {
            service.updateStock(id, cantidad);
            return new ResponseEntity<>("Stock actualizado", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/reservar/{id}/{cantidad}")
    public ResponseEntity<?> reservarStock(Long idProducto, int cantidad){
        try {
            service.reserveStock(idProducto, cantidad);
            return new ResponseEntity<>("Stock reservado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/confirmar/{id}/{cantidad}")
    public ResponseEntity<?> confirmarStock(Long idProducto, int cantidad){
        try {
            service.confirmStock(idProducto, cantidad);
            return new ResponseEntity<>("Stock confirmado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/cancelar/{id}/{cantidad}")
    public ResponseEntity<?> cancelarStock(Long idProducto, int cantidad){
        try {
            service.cancelStock(idProducto, cantidad);
            return new ResponseEntity<>("Stock cancelado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
}
