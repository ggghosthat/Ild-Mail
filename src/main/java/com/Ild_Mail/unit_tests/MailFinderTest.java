package com.Ild_Mail.unit_tests;

import com.Ild_Mail.models.finder.MailFinder;
import com.Ild_Mail.models.recieve.ReceiverIMAP;
import javax.mail.MessagingException;

class MailFinderTest {
    private MailFinder finder = new MailFinder("host","","");

    private ReceiverIMAP reciever = new ReceiverIMAP();


    public void findInterest() throws MessagingException {
//        finder.setSearchType(SearchType.ADDRESS);
//        finder.setTemplate("inst");
//        finder.search();

        reciever.build("","","");
    }
}