package com.francodavyd.controller;

import com.francodavyd.model.Producto;
import com.francodavyd.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@PreAuthorize("permitAll()")
public class ProductoController {
    @Autowired
    private IProductoService service;
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id){
        try {
            service.deleteById(id);
            return new ResponseEntity<>("Producto eliminado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editarProducto(@PathVariable Long id, @RequestBody Producto producto){
        try {
            return new ResponseEntity<>(service.update(id, producto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/stock/{id}/{cantidad}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @PathVariable Integer cantidad){
        try {
            service.updateStock(id, cantidad);
            return new ResponseEntity<>("Stock actualizado", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/reservar/{id}/{cantidad}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> reservarStock(@PathVariable("id") Long idProducto, @PathVariable("cantidad") Integer cantidad){
        try {
            service.reserveStock(idProducto, cantidad);
            return new ResponseEntity<>("Stock reservado correctamente", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente: " + e.getMessage() + e.getCause(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/confirmar/{id}/{cantidad}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> confirmarStock(Long idProducto, Integer cantidad){
        try {
            service.confirmStock(idProducto, cantidad);
            return new ResponseEntity<>("Stock confirmado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/cancelar/{id}/{cantidad}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cancelarStock(Long idProducto, Integer cantidad){
        try {
            service.cancelStock(idProducto, cantidad);
            return new ResponseEntity<>("Stock cancelado correctamente", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Lo sentimos, ha ocurrido un error. Intente nuevamente", HttpStatus.NOT_FOUND);
        }
    }
}
