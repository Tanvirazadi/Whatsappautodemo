package com.example.eminz.Model;

public class Sms {
    public String name = "";
    public String messageDate = "";
    public String messageTime = "";
    public String message = "";
    public String messageStatus = "";
    public String number;

    public Sms(String n, String ln, String md, String mt, String m, String ms) {
        name = n;
        messageDate = md;
        messageTime = mt;
        message = m;
        messageStatus = ms;
    }
}
