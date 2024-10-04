package com.francodavyd.controller;

import com.francodavyd.dto.EmailDTO;
import com.francodavyd.service.IEmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/mail")
public class EmailController {
        @Autowired
        private IEmailSenderService emailService;

        @PostMapping("/sendMessage")
        public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO){
            try {
                emailService.sendEmail(emailDTO.getToUser(), emailDTO.getSubject(), emailDTO.getMessage());
                 return new ResponseEntity<>("Correo enviado correctamente", HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>("Ha ocurrido un error, intente nuevamente",HttpStatus.BAD_REQUEST);
            }
        }

    }
