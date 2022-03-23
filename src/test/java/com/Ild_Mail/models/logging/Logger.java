package com.Ild_Mail.models.logging;

public class Logger {
    private static final StringBuilder strBuilder = new StringBuilder();

    public String GetLog(){
        return strBuilder.toString();
    }

    public void PutLog(String logLine){
        strBuilder.append(logLine + "\n");
    }
}
