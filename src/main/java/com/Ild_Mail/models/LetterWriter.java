package com.Ild_Mail.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LetterWriter {
    private String _subject = null;
    private String[] _content = null;

    private int maxLength;

    private String subjectHeader = null;
    private String contentHeader = null;
    private String underline = null;


    public LetterWriter(){}

    public LetterWriter(String subject, String[] content){
        _subject = subject;
        _content = content;
        maxLength = DefineLineLength();
        PrepareHeaders();
    }

    private int DefineLineLength(){
        List<Integer> lengths = new ArrayList<Integer>();
        lengths.add(_subject.length());
        for (String line : _content)
            lengths.add(line.length());

        return Collections.max(lengths);
    }

    private void PrepareHeaders(){
        if(maxLength > 13){
            subjectHeader = "___SUBJECT___";
            contentHeader = "___CONTENT___";
            underline = "_____________";
            int difference = maxLength - 13;
            for(int i = 0; i < difference;) {
                subjectHeader += "_";
                contentHeader += "_";
                underline += "_";
            }
        }
        else if (maxLength == 13 || maxLength < 13){
            subjectHeader = "___SUBJECT___";
            contentHeader = "___CONTENT___";
            underline = "_____________";
        }
    }

    public List<String> WriteLetter(){
        List<String> raw = new ArrayList<String>();

        raw.add(subjectHeader + "\n");
        raw.add(_subject + "\n");
        raw.add(contentHeader + "\n");

        for(String line : _content)
            raw.add(line + "\n");

        raw.add(underline + "\n");

        return raw;
    }
}
