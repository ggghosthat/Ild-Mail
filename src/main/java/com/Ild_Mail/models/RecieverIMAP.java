package com.Ild_Mail.models;

import javax.mail.*;
import javax.mail.internet.AddressException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RecieverIMAP {
    private String host = null;
    private String address = null;
    private String password = null;
    private Session session = null;
    private Store store;
    private List<Message> messages = new ArrayList<Message>();

    public List<Message> getMessages (){
        return this.messages;
    }

    public RecieverIMAP(String host, String address, String password) throws AddressException {
        this.host = host;
        this.address = address;
        this.password = password;
        CleanDirectory();
    }

    private void GenerateSession(){
        Properties properties = new Properties();
        properties.setProperty("mail.imap.port","993");
        properties.setProperty("mail.imap.ssl.enable","true");
        properties.setProperty("mail.store.protocol","imaps");
        session = session.getDefaultInstance(properties,null);
        session.setDebug(true);
    }

    private void InitStore() throws MessagingException {
        store = session.getStore("imaps");
        store.connect(host, address, password);
    }

    private void LookFolders() throws MessagingException, IOException {
        System.out.println("Please, wait ...");
        Folder folder = store.getFolder("INBOX");
        int count = folder.getMessageCount();

        folder.open(Folder.READ_WRITE);
        for (int i =1; i < count; i++){
            messages.add(folder.getMessage(i));

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

    public void CleanDirectory(){
        if(new File("./session").exists())
            new File("./session").delete();
    }
}
