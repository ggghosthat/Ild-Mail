package com.Ild_Mail.models.recieve;

import com.Ild_Mail.models.letter_model.LetterPOJO;
import org.parboiled.common.FileUtils;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Unwraper implements Runnable {

    private static String alloc_path = ".session/";

    private UUID id = UUID.randomUUID();
    private String message_alloc = alloc_path + id + '/';
    private Message message = null;
    private String _subject;
    private Object _content;

    private List<LetterPOJO> letterPOJOs = new ArrayList<LetterPOJO>();
    private List<Multipart> multipartLetters = new ArrayList<Multipart>();
    private List<String> textLetters = new ArrayList<String>();


    public Unwraper(String alloc_box){
        if (alloc_box != null)
            alloc_path = alloc_box;
        CheckSessionAllocPath();
    }


    @Override
    public void run() {
        try {
            Process();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void Unwrap(Message enterMessage) throws Exception {
        message = enterMessage;
        CompletableFuture<Void> unwrapping = CompletableFuture.runAsync(this);
        unwrapping.get();
    }

    private void Process() throws Exception {
        CheckMessageAllocPath();

        _subject = message.getSubject();
        _content = message.getContent();
        ProcessTextContent(_subject, "sub");


        if(_content instanceof String){
            ProcessTextContent((String)_content, "cnt");
        }
        else if(_content instanceof  Multipart){
            Multipart multipart = (Multipart) _content;
            MultipartParse(multipart);
        }
    }


    //region Check paths
    //Check allocation path of fetched messages
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

    //Check allocation path of single message
    private void CheckMessageAllocPath(){
        File alloc_file = new File(message_alloc);
        if (!alloc_file.exists())
            alloc_file.mkdir();
    }
    //endregion

    //region Message Parse Methods
    //This methods're parsing incomes
    private void MultipartParse(Multipart multipartMessage) throws  Exception{
        int count = multipartMessage.getCount();

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipartMessage.getBodyPart(i);
            String[] cids = bodyPart.getHeader("Content-Id");

            String cid="", content = "";
            if(cids != null && cids.length > 0){
                content = cids[0];

                if (content.startsWith("<") && content.endsWith(">")) {
                    cid = "cid:" + content.substring(1, content.length() - 1);
                } else {
                    cid = "cid:" + content;
                }
            }

            if(bodyPart.isMimeType("text/plain")){
                String plain_text_banner = "[MAIL] Plain text letter : \n\t" + bodyPart.getContent();
                ProcessTextContent(plain_text_banner, null);
            }
            else if(bodyPart.isMimeType("text/html")){
                String html_text_banner = "[MAIL] HTML letter \n\t:" + bodyPart.getContent();
                ProcessHtmlContent(html_text_banner, null);
            }
            else if (bodyPart.isMimeType("multipart/*")) {
                Multipart part = (Multipart)bodyPart.getContent();
                MultipartParse(part);
            }
            else if (bodyPart.isMimeType("application/octet-stream")) {
                String disposition = bodyPart.getDisposition();
                String binraw_banner = "[MAIL] binary raw:" + disposition;

                if (disposition.equalsIgnoreCase(BodyPart.ATTACHMENT)) {
                    ProcessAttachment(bodyPart);
                }
            }
            else if (bodyPart.isMimeType("image/*") && !("".equals(cid))) {
                ProcessEmbeddedImage(bodyPart);
            }
        }
    }

    private void ProcessTextContent(String plain, String name){
        String alloc_plain_name;
        if (name != null)
            alloc_plain_name = name;
        else
            alloc_plain_name = "plain";

        File file = new File(message_alloc + alloc_plain_name + ".txt");
        FileUtils.writeAllText(plain, file);
        System.out.println(message_alloc);
    }

    private void ProcessHtmlContent(String hyper, String name){
        String alloc_hyper_name;
        if (name != null)
            alloc_hyper_name = name;
        else
            alloc_hyper_name = "pag";

        File file = new File(message_alloc + alloc_hyper_name + ".html");
        FileUtils.writeAllText(hyper, file);
    }

    private void ProcessEmbeddedImage(BodyPart bodyPart) throws MessagingException, IOException {
        DataHandler dataHandler = bodyPart.getDataHandler();
        String name = dataHandler.getName();

        InputStream is = dataHandler.getInputStream();
        File file = new File(message_alloc + name);
        copy(is, new FileOutputStream(file));
        System.out.println("[IMG] - " + message_alloc);
    }

    private void ProcessAttachment(BodyPart bodyPart) throws MessagingException, IOException {
        InputStream is = bodyPart.getInputStream();
        File file = new File(message_alloc + bodyPart.getFileName());
        copy(is, new FileOutputStream(file));
        System.out.println("[ATTACH] - " + message_alloc);
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
    //endregion


}

