package com.Ild_Mail.unit_tests;

import com.Ild_Mail.models.recieve.ReceiverPOP;

public class POP3ConnectionTest {
    private String host = "pop.gmail.com";
    private String address = "ildarildar990@gmail.com";
    private String password = "yvhbgtqjealkkvcg";
    private ReceiverPOP pop = new ReceiverPOP(host, address, password, "");


    @org.junit.jupiter.api.Test
    public void TestPop3(){

        try{
            pop.Connect();
            pop.ExtractUnread();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
