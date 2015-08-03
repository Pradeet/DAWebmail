package com.sigmobile.dawebmail;

import com.orm.SugarRecord;

public class SentEmailMessage extends SugarRecord<SentEmailMessage> {

    public String fromname = "";
    public String fromaddress = ""; // new
    public String subject = "";
    public String subjectfull = "";
    public String date = "";
    public String dateentire = ""; // new
    public String contentlink = "";
    public String readunread = "";
    public String content = "";
    public String attlink1 = "";
    public String attlink2 = "";
    public String attlink3 = "";

    public String allto = "";
    public String cc = "";
    public String bcc;

    SentEmailMessage(){
    }

    public SentEmailMessage(String fromname, String fromaddress, String subject,
                            String subjectfull, String date, String dateentire,
                            String contentlink, String readunread, String content,
                            String attlink1, String attlink2, String attlink3, String allto, String cc, String bcc) {
        this.fromname = fromname;
        this.fromaddress = fromaddress;
        this.subject = subject;
        this.subjectfull = subjectfull;
        this.date = date;
        this.dateentire = dateentire;
        this.contentlink = contentlink;
        this.readunread = readunread;
        this.content = content;
        this.attlink1 = attlink1;
        this.attlink2 = attlink2;
        this.attlink3 = attlink3;
        this.allto = allto;
        this.cc = cc;
        this.bcc = bcc;
    }
}
