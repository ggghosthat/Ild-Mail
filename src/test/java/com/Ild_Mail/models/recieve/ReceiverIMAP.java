package com.Ild_Mail.models.recieve;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class ReceiverIMAP {
    private static String host = null;
    private static String address = null;
    private static String password = null;

    private static Boolean isUsingProxy = false;

    private static String proxy_host = null;
    private static String proxy_port = null;
    private static String proxy_user = null;
    private static String proxy_password = null;

    private static String allocation = null;

    private static Session session = null;
    private static Store store;
    private static Unwrapper unwrapper;


    private static List<Message> messages = new ArrayList<Message>();

    public List<Message> getMessages (){
        return this.messages;
    }




    public ReceiverIMAP(String host, String address, String password, String allocation) {
        this.host = host;
        this.address = address;
        this.password = password;
        this.allocation = allocation;
    }

    public ReceiverIMAP(String host, String address, String password, String allocation,
                        String proxy_host, String proxy_port, String proxy_user, String proxy_password) {
        this.host = host;
        this.address = address;
        this.password = password;
        this.allocation = allocation;

        this.proxy_host = proxy_host;
        this.proxy_port = proxy_port;
        this.proxy_user = proxy_user;
        this.proxy_password = proxy_password;

        this.isUsingProxy = true;
    }




    //generating mail session for mail server connection
    private void GenerateSession(){
        Properties properties = new Properties();
        properties.setProperty("mail.imap.port","993");
        properties.setProperty("mail.imap.ssl.enable","true");
        properties.setProperty("mail.store.protocol","imaps");

        if(isUsingProxy){
            properties.setProperty("mail.imap.proxy.host",this.proxy_host);
            properties.setProperty("mail.imap.proxy.port",this.proxy_port);

            if(this.proxy_user !=  null && this.proxy_password != null && this.proxy_user !=  "" && this.proxy_password != "") {
                properties.setProperty("mail.imap.proxy.user", this.proxy_user);
                properties.setProperty("mail.imap.proxy.password", this.proxy_password);
            }
        }

        session = session.getDefaultInstance(properties,null);
        session.setDebug(true);
    }

    //initializing session store for communicating with mail messages
    private void InitStore() throws MessagingException {
        store = session.getStore("imaps");
        store.connect(host, address, password);
    }

    //Fetching messages from mail box & converting
    private void LookFolders() throws MessagingException, IOException {
        System.out.println("Please, wait ...");
        Folder folder = store.getFolder("INBOX");
        int count = folder.getMessageCount();

        folder.open(Folder.READ_WRITE);
        for (int i =1; i < count; i++){
            messages.add(folder.getMessage(i));
        }

        unwrapper = new Unwrapper(messages, allocation);
        ConvertIncomesAsync();

        System.out.println("All letters were recieved !");
    }

    //Converting incomes to LetterIMAP struct
    private void ConvertIncomesAsync(){
        System.out.println("[INFO] - Unwrapping income mail messages ...");

        try {
            System.out.println("[INFO] - converting income mail messages ...");
            CompletableFuture<Void> result = CompletableFuture.runAsync(unwrapper);
            System.out.println("Please, wait for result");
            result.get();
            messages = null;
            System.out.println("[INFO] - converting income mail messages completed !");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }



    public void LookIntoBox(){
        try{
            GenerateSession();
            InitStore();
            LookFolders();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //TODO:Need method for cleaning processes

}
