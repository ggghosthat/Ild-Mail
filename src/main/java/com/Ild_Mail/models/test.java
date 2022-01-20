package com.Ild_Mail.models;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
	//Dysfunctional params
        RecieverIMAP imap = new RecieverIMAP("imap.gmail.com","ildarildar990@gmail.com", "7845129630.qwe");
        imap.LookIntoBox();
        System.out.println("Fetching...");
        List<Message> messages = imap.getMessages();
        System.out.println("Fetched");
        for(Message mes : messages){
            LetterIMAP letterIMAP = new LetterIMAP(mes);
            System.out.println(letterIMAP.getSubject());
        }
    }
}
