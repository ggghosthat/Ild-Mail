package com.Ild_Mail.models.input_reader;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class LetterPad {
    //letter contains
    private String _subject;
    private String _staff;

    private Scanner scanner;

    public LetterPad(){
        ReadPad();
    }

    public LetterPad(String path){
        ReadFile(path);
    }

    
    public String GetStaff(){
        return  _staff;
    }


    //read message from file
    private void ReadFile(String path){
        try{
            File file = new File(path);
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                this._staff += scanner.nextLine();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    //read message from pad in real time
    private void ReadPad(){
        System.out.println("Enter your letter staff below : ");
        scanner = new Scanner(System.in);
        while(true){
            String input = scanner.nextLine();

            if(input.equals("<ex")) {
                _staff = null;
                break;
            }
            if(input.equals("<exs")){
                Save();
                break;
            }
            if(input.equals("<out")){
                System.out.println("Ready to send !");
                break;
            }

            this._staff += input;
        }
    }

    //save draft
    private void Save(){
        System.out.println("Please, enter path to your draft file :");

        scanner = new Scanner(System.in);
        String draftPath = scanner.nextLine();

        try(FileWriter writer = new FileWriter(draftPath)){
            writer.write(_staff);
            System.out.println("Successfully saved !");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
