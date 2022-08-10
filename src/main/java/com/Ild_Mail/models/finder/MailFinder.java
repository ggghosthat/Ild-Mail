 package com.Ild_Mail.models.finder;

import com.Ild_Mail.models.recieve.ReceiverIMAP;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MailFinder {

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

    private static ReceiverIMAP receiverIMAP = new ReceiverIMAP();

    private static SearchType searchType;

    private static String template;


    public MailFinder(String host, String address, String password) {
        receiverIMAP.build(host,address,password);
    }

    public MailFinder(String host, String address, String password,
                        String proxy_host, String proxy_port, String proxy_user, String proxy_password) {
        receiverIMAP.build(host, address, password, proxy_host, proxy_port, proxy_user, proxy_password);
    }


    public static void setSearchType(SearchType searchType) {
        MailFinder.searchType = searchType;
    }

    public static void setTemplate(String template) {
        MailFinder.template = template;
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
                    if ( message.getSubject() != null && message.getSubject().toLowerCase().contains(template))
                        foundMessages.add(message);
                    break;
                default:
                    break;
            }
        }

        return foundMessages;
    }

    public void search(Message[] messages) throws MessagingException{
        if(messages != null) {
            for (Message mes : SearchProcess(messages))
                System.out.println(mes.getSubject());
        }
    }
}
