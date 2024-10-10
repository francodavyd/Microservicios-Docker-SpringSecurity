package com.francodavyd.repository;

import com.francodavyd.dto.EmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificaciones-sv")
public interface IEmailSenderFeignClient {
    @PostMapping("/sendMessage")
    public void receiveRequestEmail(@RequestBody EmailDTO emailDTO);
}
