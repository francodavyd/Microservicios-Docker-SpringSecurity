package com.francodavyd.service;

public interface IEmailSenderService {
    public void sendEmail(String[] destino, String asunto, String mensaje);
}
