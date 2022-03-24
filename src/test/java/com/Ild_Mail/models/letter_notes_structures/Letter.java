package com.Ild_Mail.models.letter_notes_structures;

import com.Ild_Mail.Interfaces.ILetter;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Letter implements ILetter {
    private UUID _letterId = UUID.randomUUID();
    private String _subject = null;
    private String _content = null;
    private List<File> _files = new ArrayList<File>();


    private Message _message = null;
    private Multipart multipart;


    private Note letterWriter = null;


    public String getId(){
        return String.valueOf(this._letterId);
    }

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

    @Override
    public void setFiles(List<File> files) {
        if (_files == null)
            _files = files;
        else
            _files.addAll(files);
    }

    public Message getMessage(){
        return this._message;
    }


    public void AddFile(File file) {
        this._files.add(file);
    }

    public void ClearFileStructer() {
        this._files.clear();
        this._files = null;
    }

    public void RemoveFile(int index) {
        this._files.remove(index);
    }

    public void InsertFile(int index, File file) throws Exception {
       if (_files.size() > 0)
           _files.set(index, file);
       else
           _files.add(file);
    }

    public void ModifyFile(int index, File file) {
        this._files.set(index, file);
    }

    public List<String> ShowLetter(){
        letterWriter = new Note(_subject, _content.split("\n"));
        return  letterWriter.WriteNote();
    }


    public void PrepareMimeBody() throws Exception {
        MimeBodyPart _mimeBody = new MimeBodyPart();
        _mimeBody.setText(this._content);

        multipart.addBodyPart(_mimeBody);

        if (_files.size() > 0){
            MimeBodyPart fileBodyPart = new MimeBodyPart();
            for (File file : _files)
                fileBodyPart.attachFile(file);

            multipart.addBodyPart(fileBodyPart);
        }

        _message.setSubject(_subject);
        _message.setContent(multipart);

    }
}
