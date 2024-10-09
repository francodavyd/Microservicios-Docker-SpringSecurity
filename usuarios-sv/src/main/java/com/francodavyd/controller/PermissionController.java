package com.francodavyd.controller;

import com.francodavyd.model.Permission;
import com.francodavyd.service.IPermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission")
@PreAuthorize("denyAll()")
public class PermissionController {
    @Autowired
    private IPermissionService service;

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> createPermission(@Valid @RequestBody Permission permission){
        try {
            return new ResponseEntity<>(service.save(permission), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll (){
        try {
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getPermission(@PathVariable Long id){
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("The requested permission has not been found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePermission(@PathVariable Long id){
        try {
            service.deleteById(id);
            return new ResponseEntity<>("Permission successfully deleted", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePermission(@PathVariable Long id, @RequestBody Permission updatedPermission){
        try {
            return new ResponseEntity<>(service.update(id,updatedPermission), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}