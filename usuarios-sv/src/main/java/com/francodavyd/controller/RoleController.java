package com.francodavyd.controller;

import com.francodavyd.model.Permission;
import com.francodavyd.model.Role;
import com.francodavyd.service.IPermissionService;
import com.francodavyd.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
@RestController
@RequestMapping("/api/role")
@PreAuthorize("denyAll()")
public class RoleController {
    @Autowired
    private IRoleService service;
    @Autowired
    private IPermissionService serviceP;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("The requested role has not been found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Set<Permission> permiList = new HashSet<Permission>();
            Permission readPermission;

            for (Permission per : role.getPermissionsList()) {
                readPermission = serviceP.findById(per.getId()).orElse(null);
                if (readPermission != null) {
                    permiList.add(readPermission);
                }
            }
            role.setPermissionsList(permiList);
            return new ResponseEntity<>(service.save(role), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            return new ResponseEntity<>(service.update(id, role), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}