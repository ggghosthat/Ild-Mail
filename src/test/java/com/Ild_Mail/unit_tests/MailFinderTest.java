package com.Ild_Mail.unit_tests;

import static org.junit.jupiter.api.Assertions.*;
import com.Ild_Mail.models.finder.MailFinder;
import com.Ild_Mail.models.finder.SearchType;
import com.Ild_Mail.models.recieve.ReceiverIMAP;

import javax.mail.Message;
import javax.mail.MessagingException;

class MailFinderTest {
    private MailFinder finder = new MailFinder("host","","");

    private ReceiverIMAP reciever = new ReceiverIMAP();

    @org.junit.jupiter.api.Test
    public void findInterest() throws MessagingException {
//        finder.setSearchType(SearchType.ADDRESS);
//        finder.setTemplate("inst");
//        finder.search();

        reciever.build("","","");
//        reciever.ExtractUnread();
        for (Message msg : reciever.ExtractLast(10)){
            System.out.println(msg.getSubject() + "---" + msg.getReceivedDate());
        }
    }
}