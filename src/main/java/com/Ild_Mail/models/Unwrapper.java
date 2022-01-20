package com.Ild_Mail.models;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Unwrapper {
    List<Message> messages;

    String savePathway;

    List<Multipart> multipartLetters = new ArrayList<Multipart>();
    List<String> textLetters = new ArrayList<String>();

    private Logger _logger = new Logger();

    public Unwrapper (List<Message> messages)
    {
        this.messages = messages;
    }

    public void Open() throws Exception {
        for (Message mes : messages){
            Object content = mes.getContent();
            if(content instanceof String){
                _logger.PutLog("[MAIL] Plain Text Letter > " + mes.getSubject() + "\n" + content.toString());
                System.out.println("[MAIL] Plain Text Letter > " + content);
                textLetters.add((String) content);
            }
            else if(content instanceof  Multipart){
                _logger.PutLog("[MAIL] Multipart Letter > " + mes.getSubject());
                Multipart multipart = (Multipart) content;
                MultipartParse(multipart);

            }
        }
    }

    private void MultipartParse(Multipart multipartMessage) throws  Exception{
        int count = multipartMessage.getCount();

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipartMessage.getBodyPart(i);
            String[] cids = bodyPart.getHeader("Content-Id");

            String count_ids_banner = "Count of content ids : " + (cids == null ? "No content id" : cids.length);
            _logger.PutLog(count_ids_banner);
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

            _logger.PutLog(content + "---" + cid + "\n" + bodyPart.getContentType() );
            System.out.println(content + "---" + cid + "\n" + bodyPart.getContentType());

            if(bodyPart.isMimeType("text/plain")){
                String plain_text_banner = "[MAIL] Plain text letter : \n\t" + bodyPart.getContent();
                _logger.PutLog(plain_text_banner);
                System.out.println(plain_text_banner);
            }
            else if(bodyPart.isMimeType("text/html")){
                String html_text_banner = "[MAIL] HTML letter \n\t:" + bodyPart.getContent();
                _logger.PutLog(html_text_banner);
                System.out.println(html_text_banner);
            }
            else if (bodyPart.isMimeType("multipart/*")) {
                Multipart part = (Multipart)bodyPart.getContent();
                MultipartParse(part);
            }
            else if (bodyPart.isMimeType("application/octet-stream")) {
                String disposition = bodyPart.getDisposition();

                String binraw_banner = "[MAIL] binary raw:" + disposition;
                _logger.PutLog(binraw_banner);
                System.out.println(binraw_banner);

                if (disposition.equalsIgnoreCase(BodyPart.ATTACHMENT)) {
                    ProcessAttachment(bodyPart);
                }
            } else if (bodyPart.isMimeType("image/*") && !("".equals(cid))) {
                ProcessEmbeddedImage(bodyPart);
            }
        }
    }

    private void ProcessEmbeddedImage(BodyPart bodyPart) throws MessagingException, IOException {
        DataHandler dataHandler = bodyPart.getDataHandler();
        String name = dataHandler.getName();

        String image_banner = "[MAIL] embedded picture name:" + name;
        _logger.PutLog(image_banner);
        System.out.println ("[MAIL] embedded picture name:" + name);

        InputStream is = dataHandler.getInputStream();
        File file = new File( "/session/" + name);
        copy(is, new FileOutputStream(file));
    }

    private void ProcessAttachment(BodyPart bodyPart) throws MessagingException, IOException {
        String fileName = bodyPart.getFileName();
        System.out.println("[INFO] saving attachment" + fileName);
        InputStream is = bodyPart.getInputStream();
        File file = new File("./mail/"+fileName);
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

