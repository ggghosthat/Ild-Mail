package com.Ild_Mail.models;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Sender {
    private InternetAddress fromAddress = null;
    private String password = null;
    private InternetAddress toAddress = null;
    private String host = null;
    private Session session = null;
    private Message message = null;
    private Transport transport = null;

    private FileInputStream inputStream = null;

    private Multipart multipartMsg = new MimeMultipart();

    public Sender(String from, String password, String to, String host) throws AddressException {
        fromAddress = new InternetAddress(from);
        toAddress = new InternetAddress(to);
        this.password = password;
        this.host = host;
    }

    private void GenerateSession(){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", fromAddress);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(properties);
        session.setDebug(true);
    }

    private void PrepareMessage(String subject, String body) throws MessagingException {
        message = new MimeMessage(session);
        message.setFrom(fromAddress);
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{toAddress});

        message.setSubject(subject);
        message.setText(body);
    }

    private void TransfareMessage() throws MessagingException {
        transport = session.getTransport("smtp");
        transport.connect(host, fromAddress.toString(), password);
        transport.sendMessage(message, new InternetAddress[]{toAddress});
        transport.close();
    }

    public void DropMessage(String subject, String body){
        try {
            GenerateSession();
            PrepareMessage(subject, body);

            System.out.println("Sending message ...");
            TransfareMessage();
            System.out.println("Message was sent successfuly");

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            fromAddress = null;
            toAddress = null;
            session = null;
            message = null;
            return;
        }
    }

    public void AttachHtml(String subject, String text, File file) throws IOException, MessagingException {
        try {
            GenerateSession();
            PrepareFile(subject,text, file);

            System.out.println("Sending message ...");
            TransfareMessage();
            System.out.println("Message was sent successfuly");

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            fromAddress = null;
            toAddress = null;
            session = null;
            message = null;
            return;
        }
    }

    private void PrepareFile(String subject, String text, File file) throws MessagingException, IOException {
        message = new MimeMessage(session);
        MimeBodyPart mesPart = new MimeBodyPart();
        MimeBodyPart bodyPartHTML = new MimeBodyPart();

        mesPart.setText(text);
        bodyPartHTML.setText(text);

        if(file.exists()){
            bodyPartHTML.attachFile(file);
            System.out.println("File succesed");
        }
        multipartMsg.addBodyPart(mesPart);
        multipartMsg.addBodyPart(bodyPartHTML);

        message.setSubject(subject);
        message.setText(text);
        message.setContent(multipartMsg);
        message.setFrom(fromAddress);
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{toAddress});
    }
}
