package com.Ild_Mail.models.letter_notes_structures;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LetterPOJO {
    private UUID _letterId = UUID.randomUUID();
    private static String _subject;
    private static String _content;
    private LetterPOJOTag _tag;
    private List<File> _attachedFiles = new ArrayList<File>();

    private String alloc_path;
    private File mainFile = new File(alloc_path + "/main.txt");
    private File attachFile = new File(alloc_path + "/attachments");

    public enum LetterPOJOTag{
        PLAIN_TEXT, HTML_TEXT, FILES, MAIL_WITH_ATTACHMENT, IMAGES
    }


    public String get_id(){
        return _letterId.toString();
    }

    public String get_subject() {
        return _subject;
    }

    public String get_content() {
        return _content;
    }

    public List<File> get_attachedFiles() {
        return _attachedFiles;
    }

    public String get_tag(){
        switch(_tag){
            case PLAIN_TEXT:
                return "plain-text";
            case HTML_TEXT:
                return "html-text";
            case FILES:
                return "files";
            case MAIL_WITH_ATTACHMENT:
                return "mail-with-attachment";
            case IMAGES:
                return "image";
            default:
                return "none";
        }
    }

    public String getAlloc_path() {
        return alloc_path;
    }


    public void set_subject(String subject){
        _subject = subject;
    }

    public void set_content(String content){
        _content = content;
    }

    public void set_files(File file){
        _attachedFiles.add(file);
    }

    public void set_files(List<File> files){
        _attachedFiles.addAll(files);
    }

    public void set_tag(LetterPOJOTag tag){
        _tag = tag;
    }

    public void set_alloc_path(String alloc_path) {
        this.alloc_path = alloc_path;
    }


    public void DumpLetter(){
        try{
            if(!mainFile.exists())
                mainFile.createNewFile();

            if(!attachFile.exists())
                attachFile.mkdir();

            //Flushing subject & content
            PrintWriter writer = new PrintWriter(mainFile);
            String staff = FlushPattern();
            writer.write(staff);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private String FlushPattern(){
        String sub;
        String con;

        if (_subject == null)
            sub = "none";
        else
            sub = _subject;

        if (_content== null)
            con = "none";
        else
            con = _content;

        return String.format("[SUBJECT]\n\t%s\n[CONTENT]\n\t%s", sub, con);
    }
}
