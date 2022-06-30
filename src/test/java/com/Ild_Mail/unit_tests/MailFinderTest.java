package com.Ild_Mail.unit_tests;

import static org.junit.jupiter.api.Assertions.*;
import com.Ild_Mail.models.finder.MailFinder;
import com.Ild_Mail.models.finder.SearchType;

import javax.mail.MessagingException;

class MailFinderTest {
    private MailFinder finder = new MailFinder("imap.gmail.com","ildarildar990@gmail.com","7845129630.qwe");

    @org.junit.jupiter.api.Test
    public void findInterest() throws MessagingException {
//        finder.setSearchType(SearchType.ADDRESS);
//        finder.setTemplate("inst");
//        finder.search();

    }
}