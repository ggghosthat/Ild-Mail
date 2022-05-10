package com.Ild_Mail;

import com.Ild_Mail.models.letter_notes_structures.Letter;
import com.Ild_Mail.models.smtp_send.Sender;

import javax.mail.internet.AddressException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoreMain {
    static Sender sender = null;
    static String mailHost = null;
    static String from = null;
    static String password = null;
    static String to = null;


    static Scanner sc = new Scanner(System.in);

    static boolean isActive = true;
    static List<String> lines = new ArrayList<String>();
    static Letter letter = new Letter();


    public static void main(String[] args) throws AddressException {
        System.out.println("Welcome to Ild-Mail service .\n Please enter the mail host");
        mailHost = sc.nextLine();
        System.out.println("Please enter your e-mail address : \n>");
        from = sc.nextLine();
        System.out.println("Please enter password of your mail account : \n>");
        password = sc.nextLine();
        System.out.println("Please enter destination mail address : \n>");
        to = sc.nextLine();

        sender = new Sender(from,password,to,mailHost);

        System.out.println("Enter your subject");
        String subject = sc.nextLine();

        System.out.println("Enter the content of your letter");
        PrepareLetter();

        letter.setSubject(subject);
        letter.setContent(ContentFromLines());

        sender.SendMessage();
    }

    private static void PrepareLetter() {
        while(isActive){
            String line = sc.nextLine();

            if(line.equals(">ex<")){
                isActive = false;
                sc.close();
            }
            else{
                lines.add(line + "\n");
            }
        }
    }

    private static String ContentFromLines(){
        String result = null;
        if(lines != null){
            for (String line : lines){
                result +=line;
            }
        }

        return result;
    }
}
