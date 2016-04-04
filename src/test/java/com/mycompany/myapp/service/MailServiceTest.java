package com.mycompany.myapp.service;

import com.mycompany.myapp.Application;
import org.apache.commons.lang.CharEncoding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

/**
 * Created by jaherrera on 29/02/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@Transactional
public class MailServiceTest {

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Test
    public void sendEmail () {

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, CharEncoding.UTF_8);

            message.setTo("jahs.es@gmail.com");
            message.setFrom("jahs.es@gmail.com");
            message.setSubject("Prueba");
            message.setText("Hola");

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {

        }
    }
}
