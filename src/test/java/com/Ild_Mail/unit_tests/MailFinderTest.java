package com.Ild_Mail.unit_tests;

import static org.junit.jupiter.api.Assertions.*;
import com.Ild_Mail.models.finder.MailFinder;
import com.Ild_Mail.models.finder.SearchType;
import com.Ild_Mail.models.recieve.ReceiverIMAP;

import javax.mail.MessagingException;

class MailFinderTest {
    private MailFinder finder = new MailFinder("host","address","your google-app password");

    private ReceiverIMAP reciever = new ReceiverIMAP();

    @org.junit.jupiter.api.Test
    public void findInterest() throws MessagingException {
//        finder.setSearchType(SearchType.ADDRESS);
//        finder.setTemplate("inst");
//        finder.search();

        reciever.build("host","address","your password");
        reciever.ExtractUnread();
    }
}