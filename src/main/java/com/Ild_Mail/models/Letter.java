package com.Ild_Mail.models;

import com.Ild_Mail.Interfaces.ILetter;

import javax.mail.Multipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Letter implements ILetter {
    private UUID _letterId = UUID.randomUUID();
    private String _subject = null;
    private String _content = null;
    private Multipart _multipart = null;
    private List<File> _files = new ArrayList<File>();


    private Note letterWriter = null;



    public String getSubject() {
        return this._subject;
    }

    public void setSubject(String subject) {
        this._subject = subject;
    }

    public String getContent() {
        return this._content;
    }

    public void setContent(String content) {
        this._content = content;
    }

    public List<File> getFiles() {
        return this._files;
    }

    public void setFiles(List<File> files) {
        this._files = files;
    }



    public void AddFile(File file) {
        this._files.add(file);
    }

    public void Clear() {
        this._files.clear();
        this._files = null;
    }

    public void RemoveFile(int index) {
        this._files.remove(index);
    }

    public void InsertFile(int index, File file) throws Exception {
       throw new Exception("Method without anything");
    }

    public void ModifyFile(int index, File file) {
        this._files.set(index, file);
    }

    public List<String> ShowLetter(){
        letterWriter = new Note(_subject, _content.split("\n"));
        return  letterWriter.WriteLetter();
    }
}
