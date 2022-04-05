package com.Ild_Mail.models.letter_notes_structures;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;


//This class implements converting income imap message 2 LetterIMAP.java struct
//Converting process going on in multithreading
public class Docker implements Supplier<List<LetterIMAP>> {
    private List<Message> messages;
    private List<LetterIMAP> letters;


    public Docker(List<Message> messages) {
        this.messages = messages;
    }

    public List<LetterIMAP> GetLetter (){
        return letters;
    }


    private void MessageToLetter(Message message) throws MessagingException {
        Lock lock = new ReentrantLock();
        lock.lock();
        letters.add(new LetterIMAP(message, message.getFolder().getName()));
        lock.unlock();
    }

    @Override
    public List<LetterIMAP> get() {
        try {
            for (Message mes : messages)
                MessageToLetter(mes);
            return letters;
        }
        catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        return null;
    }
}
