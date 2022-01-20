package com.Ild_Mail.models;

public class Logger {
    private static final StringBuilder strBuilder = new StringBuilder();

    public String GetLog(){
        return strBuilder.toString();
    }

    public void PutLog(String logLine){
        strBuilder.append(logLine + "\n");
    }
}
