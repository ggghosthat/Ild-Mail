package com.Ild_Mail.models.recieve;

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

    private Boolean isUsingProxy = false;

    private String proxy_host = null;
    private String proxy_port = null;
    private String proxy_user = null;
    private String proxy_password = null;

    private Session session = null;
    private Store store;
    private List<Message> messages = new ArrayList<Message>();

    public RecieverIMAP(String imap_host, String imap_address, String imap_password, String host, String port, String user, String password) {
    }

    public List<Message> getMessages (){
        return this.messages;
    }

    public RecieverIMAP(String host, String address, String password) throws AddressException {
        this.host = host;
        this.address = address;
        this.password = password;
        CleanDirectory();
    }

    public RecieverIMAP(String host, String address, String password, String proxy_host, int proxy_port, String proxy_user, String proxy_password) throws AddressException {
        this.host = host;
        this.address = address;
        this.password = password;

        this.proxy_host = proxy_host;
        this.proxy_port = String.valueOf(proxy_port);
        this.proxy_user = proxy_user;
        this.proxy_password = proxy_password;

        this.isUsingProxy = true;

        CleanDirectory();
    }



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
