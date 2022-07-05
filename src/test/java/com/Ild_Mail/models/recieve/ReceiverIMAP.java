package com.Ild_Mail.models.recieve;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ReceiverIMAP implements Supplier<Message[]> {

    //region Authentication Fields
    private static String host = null;
    private static String address = null;
    private static String password = null;
    //endregion

    //region Proxy Fields
    private static Boolean isUsingProxy = false;

    private static String proxy_host = null;
    private static String proxy_port = null;
    private static String proxy_user = null;
    private static String proxy_password = null;
    //endregion

    //region Core Fields
    private static String allocation = null;
    private static Message[] messageCache = null;

    private static Session session = null;
    private static Store store = null;
    private static Folder folder = null;
    private static Unwraper unwraper = null;
    //endregion



    //region Initializers
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
    //endregion

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

    //region SessionFunctions
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
    //endregion

    //region Obtain functions
    //Obtain IMAP folders
    //This method using for fetching all messages by IMAP proto
    private Message[] ObtainMessages() throws MessagingException {
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

    private Message[] ObtainLimitedCount(int startNum, int endNum) throws MessagingException{
        folder = store.getFolder("INBOX");


        folder.open(Folder.READ_WRITE);
        return folder.getMessages(startNum, endNum);
    }

    private Message[] ObtainLast(int count) throws MessagingException{
        folder = store.getFolder("INBOX");


        folder.open(Folder.READ_WRITE);
        int end = folder.getMessageCount();
        int start = end - count;

        return folder.getMessages(start, end);
    }
    //endregion

    //region default functionality
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
    //endregion

    //region External API methods
    //fetch all messages from box
    public void ExtractAll(){
        try{
            GenerateSession();
            InitStore();
            System.out.println("Please wait ...");
            messageCache = CompletableFuture.supplyAsync(this::get).get();
            System.out.println("All letters were recieved.");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    //fetch unread messages
    public void ExtractUnread(){
        try{
            GenerateSession();
            InitStore();
            System.out.println("Please wait ...");

            messageCache = CompletableFuture.supplyAsync(supplierUnread).get();
            System.out.println("You have " + messageCache.length + " unread messages");
        }
        catch (Exception ex){
            ex.printStackTrace();
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


    //fetch range of messages
    public void ExtractRange(int start, int end){
        try{
            GenerateSession();
            InitStore();
            System.out.println("Please wait ...");

            messageCache = CompletableFuture.supplyAsync(new SupplierRange(start, end)).get();
            System.out.println(String.format("Received all messages from your range (%d,%d).", start, end));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
    private class SupplierRange implements  Supplier<Message[]> {
        private int start;
        private int end;

        public SupplierRange(int start, int end){
            this.start = start;
            this.end = end;
        }

        @Override
        public Message[] get() {
            try{
                return ObtainLimitedCount(start, end);
            }
            catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
    }

    //fetch amount of messages in last
    public void ExtractLast(int count){
        try{
            GenerateSession();
            InitStore();
            System.out.println("Please wait ...");

            messageCache = CompletableFuture.supplyAsync(new SupplierEnds(count, true)).get();
            System.out.println("Received last "+count+" messages.");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private class SupplierEnds implements  Supplier<Message[]> {
        private int count;
        private boolean isLast;

        public SupplierEnds(int count, boolean isLast){
            this.count = count;
            this.isLast = isLast;
        }

        @Override
        public Message[] get() {
            try{
                if (this.isLast)
                    return ObtainLast(this.count);
                return null;
            }
            catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
    }
    //endregion

}
