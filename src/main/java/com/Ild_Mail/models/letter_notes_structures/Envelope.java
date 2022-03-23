package com.Ild_Mail.models.letter_notes_structures;

import javax.mail.Address;
import javax.mail.Multipart;
import java.util.Date;

public class Envelope {
    private String subject = null;
    private Date sentDate = null;
    private Date recievedDate = null;
    private Address[] recipients = null;
    private Address[] from = null;
    private int size = 0;


    private String strContent = null;
    private Multipart mtpContent = null;

    private StringBuilder sb = new StringBuilder();



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

    public int getSize(){
        return this.size;
    }
    public void setSize(int size){
        this.size = size;
    }


    public Object getContent(){
        if(this.strContent != null){
            return this.strContent;
        }
        else if(this.mtpContent != null){
            return this.mtpContent;
        }
        return  null;
    }
    public void setContent(Object content){
        if(content instanceof String){
            this.strContent = (String) content;
        }
        else if (content instanceof Multipart){
            this.mtpContent = (Multipart) content;
        }
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
    }




    @Override
    public  String toString(){
        PrepareStaff();

        String subjectBlock = "Subject : \n\t" + subject + "\n";
        String size = "Size : \n\t" + this.size + "\n";
        return sb.toString() + subjectBlock + size;
    }
}

