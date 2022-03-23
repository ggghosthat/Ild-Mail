package com.Ild_Mail.models.recieve;

import com.Ild_Mail.models.letter_notes_structures.Letter;
import com.Ild_Mail.models.letter_notes_structures.LetterIMAP;
import org.parboiled.common.FileUtils;

import java.io.File;
import java.util.*;

public class RecievedBoxRegister {
    private List<LetterIMAP> imapLetter = new ArrayList<LetterIMAP>();

    private HashMap<String, String> _struct = new HashMap<String, String>();
    
    public List<LetterIMAP> GetImapLetters(){
        return this.imapLetter;
    }

    public void AddImap(LetterIMAP imap){
        this.imapLetter.add(imap);

        System.out.println(String.format("[INFO] Recieved IMAP Message with id : %s was added", imap.getId()));
    }

    public void RemoveImap(Letter imap){
        if(this.imapLetter.contains(imap)) {
            this.imapLetter.remove(imap);
            System.out.println(String.format("[INFO] Recieved IMAP Message with id : %s was removed", imap.getId()));
        }
        else{
            System.out.println(String.format("[INFO] Recieved IMAP Message with id : %s does not exist", imap.getId()));
        }
    }



    private void CompileSend(){
        for (LetterIMAP imap : this.imapLetter){
            _struct.put(imap.getSubject(),imap.getId());
        }
    }



    public void SaveStruct(){
        if((_struct != null) && (_struct.size() != 0)) {
            for (Map.Entry<String, String> entry : _struct.entrySet()) {
                Write(entry.getKey(), entry.getValue());
            }
        }
    }

    private void OpenStruct(){
        String[] lines = Read().split("\n");

        for (String line : lines){
            String[] raw = line.split("|");
            this._struct.put(raw[0],raw[1]);
        }
    }


    private void Write(String key, String value){
        String content = String.format("%s1|%s2\n",key,value);
        File file = new File("./session/srct.txt");
        FileUtils.writeAllText(content, file);
    }

    private String Read(){
        if(new File("./session/srct.txt").exists())
            return FileUtils.readAllText("./session/srct.txt");
        else{
            System.out.println("Structure file not find");
            return null;
        }
    }
}
