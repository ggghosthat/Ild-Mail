package com.Ild_Mail.models.recieve;

import javax.mail.*;
import java.util.Properties;
import java.util.function.Supplier;

public class ReceiverPOP  implements Supplier<Message[]> {
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
    public ReceiverPOP() {
    }

    public ReceiverPOP(String host, String address, String password, String allocation) {
        this.host = host;
        this.address = address;
        this.password = password;
        this.allocation = allocation;
    }

    public ReceiverPOP(String host, String address, String password, String allocation,
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
    public Message[] get(){
        try {
            return null;
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
        properties.setProperty("mail.pop3.port","995");
        properties.setProperty("mail.pop3.ssl.enable","true");
        properties.setProperty("mail.store.protocol","pop3");

        if(isUsingProxy){
            properties.setProperty("mail.pop3.proxy.host",proxy_host);
            properties.setProperty("mail.pop3.proxy.port",proxy_port);

            if(proxy_user !=  null && proxy_password != null && proxy_user !=  "" && proxy_password != "") {
                properties.setProperty("mail.pop3.proxy.user", proxy_user);
                properties.setProperty("mail.pop3.proxy.password", proxy_password);
            }
        }

        session = session.getDefaultInstance(properties,null);
        session.setDebug(true);
    }

    //initializing session store for communicating with mail messages
    private static void InitStore() throws MessagingException {
        store = session.getStore("pop3s");
        store.connect(host, address, password);
    }

    public void Connect() throws MessagingException {
        GenerateSession();
        InitStore();
    }
    //endregion
}
