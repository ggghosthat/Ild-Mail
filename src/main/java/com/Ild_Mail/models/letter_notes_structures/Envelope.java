package com.Ild_Mail.models.letter_notes_structures;

import com.Ild_Mail.Interfaces.ILetter;

import javax.mail.Address;
import javax.mail.Multipart;
import java.util.Date;

public class Envelope {
    private String subject = null;
    private Date sentDate = null;
    private Date recievedDate = null;
    private Address[] recipients = null;
    private Address[] from = null;

    private Letter letter = null;

    private StringBuilder sb = new StringBuilder();


    public Envelope(){}

    public Envelope(ILetter letter){
        this.letter = (Letter)letter;
    }


    public String getSubject(){
        return this.subject;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }

    public Date getSentDate(){
        return this.sentDate;
    }
    public void setSentDate(Date sentDate){
        this.sentDate = sentDate;
    }

    public Date getRecievedDate(){
        return this.recievedDate;
    }
    public void setRecievedDate(Date recievedDate){
        this.recievedDate = recievedDate;
    }

    public Address[] getRecipients(){
        return this.recipients;
    }
    public void setRecipients (Address[] from){
        this.from = from;
    }

    public Letter getLetter(){
        return this.letter;
    }




    private void PrepareStaff(){
        sb.append("Recipients : \n");
        for (Address addr : recipients){
            sb.append("\t" + addr.toString() + "\n");
        }
        sb.append("From : \n");
        for (Address addr : from){
            sb.append("\t" + addr.toString() + "\n");
        }

        sb.append("Sent date :\n");
        sb.append("\t" + sentDate + "\n");
        sb.append("Recieve date :\n");
        sb.append("\t" + recievedDate + "\n");
        sb.append("Letter uid :\n");
        sb.append("\t" + letter.getId() + "\n");
    }




    @Override
    public  String toString(){
        PrepareStaff();

        String subjectBlock = "Subject : \n\t" + subject + "\n";
        return sb.toString() + subjectBlock;
    }
}

