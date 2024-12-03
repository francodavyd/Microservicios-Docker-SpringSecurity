package com.francodavyd.controller;

import com.francodavyd.dto.EmailDTO;
import com.francodavyd.service.IEmailSenderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(EmailController.class)
public class EmailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmailSenderService emailService;
    @Test
    public void testReceiveRequestEmail_Success() throws Exception {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setToUser(new String[]{"test@example.com"});
        emailDTO.setSubject("Test Subject");
        emailDTO.setMessage("Test Message");


        Mockito.doNothing().when(emailService).sendEmail(
                emailDTO.getToUser(),
                emailDTO.getSubject(),
                emailDTO.getMessage()
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/mail/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "toUser": ["test@example.com"],
                      "subject": "Test Subject",
                      "message": "Test Message"
                    }
                    """))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verificar el estado HTTP 200
                .andExpect(MockMvcResultMatchers.content().string("Correo enviado correctamente")); // Verificar la respuesta
    }
}
