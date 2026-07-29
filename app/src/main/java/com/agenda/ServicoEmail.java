package com.agenda;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class ServicoEmail {

    private static final String MEU_EMAIL = "alvaroteste21@gmail.com";
    private static final String MINHA_SENHA = "utpc uiwj yash wowb"; 

    public static void enviarAlerta(String emailDestinatario, String assunto, String mensagem) {

    
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(MEU_EMAIL, MINHA_SENHA);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MEU_EMAIL));
            
            
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
            
            
            message.setSubject(assunto);


            message.setContent(mensagem, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("✅ E-mail enviado com sucesso para: " + emailDestinatario);

        } catch (MessagingException e) {
            System.err.println("❌ Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
    

