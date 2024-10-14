package com.francodavyd.controller;


import com.francodavyd.model.Role;
import com.francodavyd.model.UserSec;
import com.francodavyd.service.IRoleService;
import com.francodavyd.service.IUserSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
@PreAuthorize("denyAll()")
public class UserSecController {
    @Autowired
    private IUserSecService service;
    @Autowired
    private IRoleService serviceR;

    @PostMapping("/save")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> createUser(@RequestBody UserSec userSec) {
        try {
            Set<Role> roleList = new HashSet<Role>();
            Role readRole;
            userSec.setPassword(service.encriptPassword(userSec.getPassword()));
            for (Role role : userSec.getRolesList()){
                readRole = serviceR.findById(role.getId()).orElse(null);
                if (readRole != null) {
                    roleList.add(readRole);
                }
            }
            if (!roleList.isEmpty()) {
                userSec.setRolesList(roleList);
            } else throw new RuntimeException("The list of roles cannot be empty.");

            return new ResponseEntity<>(service.save(userSec), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("The requested user with id " + id + "has not been found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            service.deleteById(id);
            return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserSec updatedUser){
        try {
            return new ResponseEntity<>(service.update(id,updatedUser), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error, " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
