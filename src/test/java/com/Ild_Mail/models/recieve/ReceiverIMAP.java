package com.Ild_Mail.models.recieve;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ReceiverIMAP implements Supplier<Message[]> {
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
    private static Store store = null;
    private static Folder folder = null;
    private static Unwraper unwraper = null;


    private static List<Message> messages = new ArrayList<Message>();


    public ReceiverIMAP() {
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



    //build RecieverIMAP instance
    public static void build(String _host, String _address, String _password){
        host = _host;
        address = _address;
        password = _password;
    }

    //build RecieverIMAP instance
    public static void build(String _host, String _address, String _password,
                             String _proxy_host, String _proxy_port, String _proxy_user, String _proxy_password){
        host = _host;
        address = _address;
        password = _password;

        proxy_port = _proxy_port;
        proxy_host = _proxy_host;
        proxy_user = _proxy_user;
        proxy_password = _proxy_password;

        isUsingProxy = true;
    }




    @Override
    public Message[] get() {
        try {
            return ObtainMessages();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }


    //generating mail session for mail server connection
    private static void GenerateSession(){
        Properties properties = new Properties();
        properties.setProperty("mail.imap.port","993");
        properties.setProperty("mail.imap.ssl.enable","true");
        properties.setProperty("mail.store.protocol","imaps");

        if(isUsingProxy){
            properties.setProperty("mail.imap.proxy.host",proxy_host);
            properties.setProperty("mail.imap.proxy.port",proxy_port);

            if(proxy_user !=  null && proxy_password != null && proxy_user !=  "" && proxy_password != "") {
                properties.setProperty("mail.imap.proxy.user", proxy_user);
                properties.setProperty("mail.imap.proxy.password", proxy_password);
            }
        }

        session = session.getDefaultInstance(properties,null);
        session.setDebug(false);
    }

    //initializing session store for communicating with mail messages
    private static void InitStore() throws MessagingException {
        store = session.getStore("imaps");
        store.connect(host, address, password);
    }

    //Obtain IMAP folders
    //This method using for fetching all messages by IMAP proto
    private static Message[] ObtainMessages() throws MessagingException {
        folder = store.getFolder("INBOX");

        folder.open(Folder.READ_WRITE);
        return folder.getMessages();
    }

    //Obtain IMAP folders
    //This method using for fetching all messages by IMAP proto
    private Message[] ObtainUnreadMessages() throws MessagingException {
        folder = store.getFolder("INBOX");

        folder.open(Folder.READ_WRITE);
        return folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
    }

    //Fetching messages from mail box & converting
    //Here it fetching messages by IMAP proto asynchronously
    //Then we iterate them to unwrap each other
    private void LookFolders() throws Exception {
        CompletableFuture<Message[]> messagesFetched = CompletableFuture.supplyAsync(this::get);
        Message[] messages = messagesFetched.get();
        System.out.println("Found total count of messages : " + messages.length);

        for (Message message : messages){
            unwraper = new Unwraper(allocation);
            unwraper.Open(message);
        }

        System.out.println("All letters were recieved !");
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



    //external API 2 fetch messages
    public Message[] ExtractFromBox(){
        try{
            GenerateSession();
            InitStore();
            System.out.println("Please wait ...");
            return  CompletableFuture.supplyAsync(this::get).get();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    //external API 2 fetch unread messages
    public Message[] ExtractUnread(){
        try{
            GenerateSession();
            InitStore();
            System.out.println("Please wait ...");

            Message[] result = CompletableFuture.supplyAsync(supplierUnread).get();
            System.out.println("You have" + result.length + "unread messages");
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return  null;
        }
    }

    Supplier<Message[]> supplierUnread = new Supplier<Message[]>() {
        @Override
        public Message[] get() {
            try{
                return ObtainUnreadMessages();
            }
            catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
    };
}
