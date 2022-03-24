package com.Ild_Mail.models.recieve;

import com.Ild_Mail.models.letter_notes_structures.Letter;
import com.Ild_Mail.models.letter_notes_structures.LetterIMAP;
import org.parboiled.common.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxRegister {
    private List<LetterIMAP> imapLetter = new ArrayList<LetterIMAP>();

    private HashMap<String, String> _struct = new HashMap<String, String>();
    
    public List<LetterIMAP> GetImapLetters(){
        return this.imapLetter;
    }



    //Adding letter from struct
    public void AddImap(LetterIMAP imap){
        this.imapLetter.add(imap);

        System.out.println(String.format("[INFO] Recieved IMAP Message with id : %s was added", imap.getId()));
    }

    //Removing letter from struct
    public void RemoveImap(Letter imap){
        if(this.imapLetter.contains(imap)) {
            this.imapLetter.remove(imap);
            System.out.println(String.format("[INFO] Recieved IMAP Message with id : %s was removed", imap.getId()));
        }
        else{
            System.out.println(String.format("[INFO] Recieved IMAP Message with id : %s does not exist", imap.getId()));
        }
    }


    //Gathering IMAP messages to special struct
    private void CompileSend(){
        for (LetterIMAP imap : this.imapLetter){
            _struct.put(imap.getSubject(),imap.getId());
        }
    }

    //Dump struct to filesystem
    public void SaveStruct(){
        if((_struct != null) && (_struct.size() != 0)) {
            for (Map.Entry<String, String> entry : _struct.entrySet()) {
                Write(entry.getKey(), entry.getValue());
            }
        }
    }

    //Search struct in filesystem
    private void OpenStruct(){
        String[] lines = Read().split("\n");

        for (String line : lines){
            String[] raw = line.split("|");
            this._struct.put(raw[0],raw[1]);
        }
    }

    //Flushing struct to filesystem
    private void Write(String key, String value){
        String content = String.format("%s1|%s2\n",key,value);
        File file = new File("./session/srct.txt");
        FileUtils.writeAllText(content, file);
    }

    //Reading struct from filesystem
    private String Read(){
        if(new File("./session/srct.txt").exists())
            return FileUtils.readAllText("./session/srct.txt");
        else{
            System.out.println("Structure file not find");
            return null;
        }
    }
}
