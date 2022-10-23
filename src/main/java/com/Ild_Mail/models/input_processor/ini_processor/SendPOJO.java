package com.Ild_Mail.models.input_processor.ini_processor;

import java.util.ArrayList;
import java.util.List;

public class SendPOJO {
    private String subject;
    private String content;
    private boolean isFile;

    private List<String> attach = new ArrayList<>();

    public String getSubject() {return subject;}
    public void setSubject(String subject) {this.subject=subject;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public boolean getIsFile() {return isFile;}
    public void setIsFile(boolean isFile) {this.isFile = isFile;}

    public List<String> getAttach() {return attach;}
    public void setAttach(List<String> attach) {this.attach=attach;}
}
