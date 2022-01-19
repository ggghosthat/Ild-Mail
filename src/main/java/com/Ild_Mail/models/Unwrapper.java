package com.Ild_Mail.models;

import org.parboiled.common.FileUtils;
import sun.misc.IOUtils;
import sun.reflect.misc.FieldUtil;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Unwrapper {
    List<Message> messages;

    String savePathway;

    List<Multipart> multipartLetters = new ArrayList<Multipart>();
    List<String> textLetters = new ArrayList<String>();

    public Unwrapper (List<Message> messages, String savePathway)
    {
        this.messages = messages;
        this.savePathway = savePathway;
    }

    public void Open() throws IOException, MessagingException {
        Multipart multipart = null;
        for (Message mes : messages){
            Object content = mes.getContent();
            if(content instanceof String){
                textLetters.add((String) content);
            }
            else if(content instanceof  Multipart){
                multipart = (Multipart) content;
            }
        }
    }

    private void UnwrapMultipartMsg(Multipart multipart)
    {   try {
            for (int x = 0; x < multipart.getCount(); x++) {
                BodyPart bodyPart = multipart.getBodyPart(x);
                String disposition = bodyPart.getDisposition();

                if(disposition != null && (disposition.equals(BodyPart.ATTACHMENT)))
                {
                    System.out.println("Mail have some attachment : ");

                    DataHandler handler = bodyPart.getDataHandler();
                    SaveAttachment(handler.getName(), handler.getInputStream());
                    System.out.println("file name : " + handler.getName());
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            return;
        }
    }

    private void SaveAttachment(String name, InputStream inputStream)
    {
        File file = new File("/sessions/" + name);
        try(FileOutputStream fos = new FileOutputStream(file)){
            byte[] buf = new byte[4096];
            int bytesRead;
            while((bytesRead = inputStream.read(buf))!=-1) {
                fos.write(buf, 0, bytesRead);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
