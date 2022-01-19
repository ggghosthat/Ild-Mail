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

    public Unwrapper (List<Message> messages)
    {
        this.messages = messages;
    }

    public void Open() throws Exception {
        for (Message mes : messages){
            Object content = mes.getContent();
            if(content instanceof String){
                System.out.println("Plain Text Letter > " + content);
                textLetters.add((String) content);
            }
            else if(content instanceof  Multipart){
                Multipart multipart = (Multipart) content;
                MultipartParse(multipart);
            }
        }
    }

    private void UnwrapMultipartMsg(Multipart multipart){
        try {
            for (int x = 0; x < multipart.getCount(); x++) {
                BodyPart bodyPart = multipart.getBodyPart(x);
                String disposition = bodyPart.getDisposition();

                if(disposition != null && (disposition.equals(BodyPart.ATTACHMENT))){
                    System.out.println("Mail have some attachment : ");

                    DataHandler handler = bodyPart.getDataHandler();
                    SaveAttachment(handler.getName(), handler.getInputStream());
                    System.out.println("file name : " + handler.getName());
                }
                else{
                    System.out.println("Content : --- " + bodyPart);
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

    private void SaveAttachment(String name, InputStream inputStream){
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


    private void MultipartParse(Multipart multipartMessage) throws  Exception{
        int count = multipartMessage.getCount();

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipartMessage.getBodyPart(i);
            String[] cids = bodyPart.getHeader("Content-Id");
            System.out.println("===> cids : " + (cids == null ? "No content id" : cids.length));

            String cid="", content = "";
            if(cids != null && cids.length > 0){
                content = cids[0];

                if (content.startsWith("<") && content.endsWith(">")) {
                    cid = "cid:" + content.substring(1, content.length() - 1);
                } else {
                    cid = "cid:" + content;
                }
            }

            System.out.println(content+"---"+cid);
            System.out.println(bodyPart.getContentType());

            if(bodyPart.isMimeType("text/plain")){
                System.out.println("Simple text letter : \n\t" + bodyPart.getContent() );
            }
            else if(bodyPart.isMimeType("text/html")){
                System.out.println("HTML letter \n\t:" + bodyPart.getContent());
            }
            else if (bodyPart.isMimeType("multipart/*")) {
                Multipart part = (Multipart)bodyPart.getContent();
                MultipartParse(part);
            }
            else if (bodyPart.isMimeType("application/octet-stream")) {
                String disposition = bodyPart.getDisposition();
                System.out.println("binary raw:" + disposition);
                if (disposition.equalsIgnoreCase(BodyPart.ATTACHMENT)) {
                    ProcessAtchment(bodyPart);
                }
            } else if (bodyPart.isMimeType("image/*") && !("".equals(cid))) {
                ProcessEmbeddedImage(bodyPart);
            }
        }
    }

    private void ProcessEmbeddedImage(BodyPart bodyPart) throws MessagingException, IOException {
        DataHandler dataHandler = bodyPart.getDataHandler();
        String name = dataHandler.getName();
        System.out.println ("embedded picture name:" + name);
        InputStream is = dataHandler.getInputStream();
        File file = new File( "/session/" + name);
        copy(is, new FileOutputStream(file));
    }

    private void ProcessAtchment(BodyPart bodyPart) throws MessagingException, IOException {
        String fileName = bodyPart.getFileName();
        System.out.println("----------------- save attachment" + fileName);
        InputStream is = bodyPart.getInputStream();
        File file = new File("C:/Users/AB/Desktop/mail/"+fileName);
        copy(is, new FileOutputStream(file));
    }

    private void copy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len=is.read(bytes)) != -1 ) {
            os.write(bytes, 0, len);
        }
        if (os != null)
            os.close();
        if (is != null)
            is.close();
    }
}

