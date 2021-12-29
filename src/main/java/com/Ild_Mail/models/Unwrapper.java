package com.Ild_Mail.models;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;
import java.util.List;

public class Unwrapper {
    List<Message> messages;

    List<Multipart> multipartLetters;
    List<String> textLetters;

    public Unwrapper (List<Message> messages){
        this.messages = messages;
    }

    public void Open() throws IOException, MessagingException {
        Multipart multipart = null;
        String strContent = null;
        for (Message mes : messages){
            Object content = mes.getContent();
            if(content instanceof String){

            }
            else if(content instanceof  Multipart){
                content.get
            }
        }
    }
}
