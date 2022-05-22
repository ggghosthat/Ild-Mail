package com.Ild_Mail.models.finder;


import javax.mail.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;


public class MailFinder implements Supplier<Message[]> {

    private static String host = null;
    private static String address = null;
    private static String password = null;

    private static Boolean isUsingProxy = false;

    private static String proxy_host = null;
    private static String proxy_port = null;
    private static String proxy_user = null;
    private static String proxy_password = null;

    private static Session session = null;
    private static Store store = null;

    private static SearchType searchType;


    private static String template;


    public MailFinder(String host, String address, String password) {
        this.host = host;
        this.address = address;
        this.password = password;
    }

    public MailFinder(String host, String address, String password,
                        String proxy_host, String proxy_port, String proxy_user, String proxy_password) {
        this.host = host;
        this.address = address;
        this.password = password;

        this.proxy_host = proxy_host;
        this.proxy_port = proxy_port;
        this.proxy_user = proxy_user;
        this.proxy_password = proxy_password;

        this.isUsingProxy = true;
    }


    public static void setSearchType(SearchType searchType) {
        MailFinder.searchType = searchType;
    }

    public static void setTemplate(String template) {
        MailFinder.template = template;
    }



    @Override
    public Message[] get() {
        try {
            return FetchFolderMessages();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
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
        session.setDebug(false);
    }

    //Fetching messages from mail box & converting
    private Message[] FetchFolderMessages() throws Exception {
        System.out.println("Please, wait ...");
        Folder folder = store.getFolder("INBOX");
        int count = folder.getMessageCount();

        folder.open(Folder.READ_WRITE);
        return folder.getMessages();
    }

    private Message[] ObtainMessagesAsync(){
        try {
            CompletableFuture<Message[]> messages = CompletableFuture.supplyAsync(this::get);
            return messages.get();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void SearchByType() throws MessagingException{
        Message[] messages = ObtainMessagesAsync();
        if(messages != null) {
            SearchProcess(messages);
        }
    }

    private List<Message> SearchProcess(Message[] messages) throws MessagingException {
        List<Message> foundMessages = new ArrayList<>();
        for (Message message : messages) {
            switch (searchType) {
                case SENT_DATE:
                    if(message.getSentDate().equals(Date.parse(template)) )
                        foundMessages.add(message);
                    break;
                case RECIEVED_DATE:
                    if(message.getReceivedDate().equals(Date.parse(template)) )
                        foundMessages.add(message);
                    break;
                case ADDRESS:
                    for (Address address : message.getAllRecipients())
                        if(address.toString().toLowerCase().contains(template))
                            foundMessages.add(message);
                    break;
                case SUBJECT:
                    if (message.getSubject().toLowerCase().contains(template))
                        foundMessages.add(message);
                    break;
                default:
                    break;
            }
        }

        return foundMessages;
    }


}
