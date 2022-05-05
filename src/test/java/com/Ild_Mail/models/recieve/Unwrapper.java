package com.Ild_Mail.models.recieve;

import com.Ild_Mail.models.letter_notes_structures.LetterPOJO;
import com.Ild_Mail.models.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Unwrapper implements Runnable {
    private static String alloc_path = "./session/";

    private List<Message> messages;

    private List<LetterPOJO> letterPOJOs = new ArrayList<LetterPOJO>();
    private List<Multipart> multipartLetters = new ArrayList<Multipart>();
    private List<String> textLetters = new ArrayList<String>();


    public Unwrapper (List<Message> messages)
    {
        this.messages = messages;
        CheckSessionAllocPath();
    }


    public void Open() throws Exception {
        for (Message mes : messages){
            LetterPOJO pojo = new LetterPOJO();

            String message_alloc = CheckMessageAllocPath(pojo.get_id());
            pojo.set_alloc_path(message_alloc);
            pojo.set_subject(mes.getSubject());
            Object content = mes.getContent();

            if(content instanceof String){
                pojo.set_content((String) content);
            }
            else if(content instanceof  Multipart){
                Multipart multipart = (Multipart) content;
                MultipartParse(multipart, pojo);
            }
        }
    }

    @Override
    public void run() {
        try {
            Open();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //This methods're parsing incomes
    private void MultipartParse(Multipart multipartMessage, LetterPOJO pojo) throws  Exception{
        int count = multipartMessage.getCount();

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipartMessage.getBodyPart(i);
            String[] cids = bodyPart.getHeader("Content-Id");

            String count_ids_banner = "Count of content ids : " + (cids == null ? "No content id" : cids.length);
            System.out.println(count_ids_banner);

            String cid="", content = "";
            if(cids != null && cids.length > 0){
                content = cids[0];

                if (content.startsWith("<") && content.endsWith(">")) {
                    cid = "cid:" + content.substring(1, content.length() - 1);
                } else {
                    cid = "cid:" + content;
                }
            }

            System.out.println(content + "---" + cid + "\n" + bodyPart.getContentType());

            if(bodyPart.isMimeType("text/plain")){
                String plain_text_banner = "[MAIL] Plain text letter : \n\t" + bodyPart.getContent();
                pojo.set_tag(LetterPOJO.LetterPOJOTag.PLAIN_TEXT);
                pojo.set_content(bodyPart.getContent().toString());
                System.out.println(plain_text_banner);
            }
            else if(bodyPart.isMimeType("text/html")){
                String html_text_banner = "[MAIL] HTML letter \n\t:" + bodyPart.getContent();
                pojo.set_tag(LetterPOJO.LetterPOJOTag.HTML_TEXT);
                pojo.set_content(bodyPart.getContent().toString());
                System.out.println(html_text_banner);
            }
            else if (bodyPart.isMimeType("multipart/*")) {
                Multipart part = (Multipart)bodyPart.getContent();
                MultipartParse(part, pojo);
            }
            else if (bodyPart.isMimeType("application/octet-stream")) {
                String disposition = bodyPart.getDisposition();
                String binraw_banner = "[MAIL] binary raw:" + disposition;
                System.out.println(binraw_banner);

                if (disposition.equalsIgnoreCase(BodyPart.ATTACHMENT)) {
                    ProcessAttachment(bodyPart, pojo);
                }
            }
            else if (bodyPart.isMimeType("image/*") && !("".equals(cid))) {
                ProcessEmbeddedImage(bodyPart, pojo);
            }
        }
    }

    private void ProcessEmbeddedImage(BodyPart bodyPart, LetterPOJO pojo) throws MessagingException, IOException {
        DataHandler dataHandler = bodyPart.getDataHandler();
        String name = dataHandler.getName();

        InputStream is = dataHandler.getInputStream();
        File file = new File(pojo.getAlloc_path() + name);
        copy(is, new FileOutputStream(file));
        pojo.set_tag(LetterPOJO.LetterPOJOTag.IMAGES);
        pojo.set_files(file);

    }

    private void ProcessAttachment(BodyPart bodyPart, LetterPOJO pojo) throws MessagingException, IOException {
        InputStream is = bodyPart.getInputStream();
        File file = new File(pojo.getAlloc_path() + bodyPart.getFileName());
        copy(is, new FileOutputStream(file));
        pojo.set_tag(LetterPOJO.LetterPOJOTag.FILES);
        pojo.set_files(file);
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


    private void CheckSessionAllocPath(){
        try {
            File alloc_file = new File(alloc_path);
            if (!alloc_file.exists())
                alloc_file.mkdir();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            return;
        }
    }

    private String CheckMessageAllocPath(String messageId){
        String result = alloc_path + messageId + "/";
        try {
            File alloc_file = new File(result);
            if (!alloc_file.exists())
                alloc_file.mkdir();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            return result;
        }
    }
}

