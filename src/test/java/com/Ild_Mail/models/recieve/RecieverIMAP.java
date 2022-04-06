package com.Ild_Mail.models.recieve;

import com.Ild_Mail.models.letter_notes_structures.Docker;
import com.Ild_Mail.models.letter_notes_structures.LetterIMAP;

import javax.mail.*;
import javax.mail.internet.AddressException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

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

    private List<LetterIMAP> convertedLetters;

    public List<Message> getMessages (){
        return this.messages;
    }




    public RecieverIMAP(String host, String address, String password) throws AddressException {
        this.host = host;
        this.address = address;
        this.password = password;
        CleanDirectory();
    }

    public RecieverIMAP(String host, String address, String password,
                        String proxy_host, String proxy_port, String proxy_user, String proxy_password) throws AddressException {
        this.host = host;
        this.address = address;
        this.password = password;

        this.proxy_host = proxy_host;
        this.proxy_port = proxy_port;
        this.proxy_user = proxy_user;
        this.proxy_password = proxy_password;

        this.isUsingProxy = true;

        CleanDirectory();
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
        LetterIMAP letter;
        int count = folder.getMessageCount();

        folder.open(Folder.READ_WRITE);
        for (int i =1; i < count; i++){
            messages.add(folder.getMessage(i));
        }

        ConvertIncomesAsynch();

        System.out.println("All letters were recieved !");
    }

    //Converting incomes to LetterIMAP struct
    private void ConvertIncomesAsynch(){
        System.out.println("[INFO] - Working on converting income mail messages ...");
        Supplier<List<LetterIMAP>> task = (Supplier<List<LetterIMAP>>) new Docker(messages).get();

        CompletableFuture<List<LetterIMAP>> result =
                                         CompletableFuture.supplyAsync(task);


        try {
            convertedLetters = result.get();
            System.out.println("[INFO] - converting income mail messages completed !");
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
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

    public void CleanDirectory(){
        if(new File("./session").exists())
            new File("./session").delete();
    }


}
