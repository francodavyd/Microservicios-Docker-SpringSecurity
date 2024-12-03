package com.francodavyd.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderServiceImpl emailService;
   @BeforeEach
   public void setUp(){
       ReflectionTestUtils.setField(emailService, "emailUser", "noreply@example.com");
   }


    @Test
    public void SendEmailTest() {
        String[] toUser = {"test@example.com"};
        String subject = "Test Subject";
        String message = "Test Message";

        emailService.sendEmail(toUser, subject, message);

        // Capture the argument passed to JavaMailSender
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(captor.capture());

        // Assert
        SimpleMailMessage capturedMessage = captor.getValue();
        assertEquals("noreply@example.com", capturedMessage.getFrom());
        assertEquals(toUser, capturedMessage.getTo());
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(message, capturedMessage.getText());
    }
}
