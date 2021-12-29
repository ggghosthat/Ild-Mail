package com.Ild_Mail.models;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws MessagingException, IOException {
	//Dysfunctional params
        RecieverIMAP imap = new RecieverIMAP("***","***", "***");
        imap.LookIntoBox();
        Unwrapper unwrapper = new Unwrapper(imap.getMessages());
        unwrapper.Open();
    }
}
