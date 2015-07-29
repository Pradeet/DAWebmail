package com.sigmobile.dawebmail;

import com.orm.SugarRecord;

public class EmailMessage extends SugarRecord {

    // so i didnt think enough. Haha. Will have to add more fields to this table
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

    public EmailMessage() {
    }

    public EmailMessage(String f, String fa, String sub, String subfull,
                        String d, String de, String rur, String conlink, String con,
                        String atlink1, String atlink2, String atlink3, String allto,
                        String cc, String bcc) {
        this.fromname = f;
        this.fromaddress = fa;
        this.subject = sub;
        this.subjectfull = subfull;
        this.date = d;
        this.dateentire = de;
        this.readunread = rur;
        this.contentlink = conlink;
        this.content = con;
        this.attlink1 = atlink1;
        this.attlink2 = atlink2;
        this.attlink3 = atlink3;
        this.allto = allto;
        this.cc = cc;
        this.bcc = bcc;
    }

    public String getFromaddress() {
        return fromaddress;
    }

    public void setFromaddress(String fromaddress) {
        this.fromaddress = fromaddress;
    }

    public String getDateentire() {
        return dateentire;
    }

    public void setDateentire(String dateentire) {
        this.dateentire = dateentire;
    }

    public String getReadunread() {
        return readunread;
    }

    public void setReadunread(String readunread) {
        this.readunread = readunread;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAllsenders() {
        return allto;
    }

    public void setAllsenders(String allsenders) {
        this.allto = allsenders;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getFromName() {
        return fromname;
    }

    public void setFromName(String from) {
        this.fromname = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContentlink() {
        return contentlink;
    }

    public void setContentlink(String contentlink) {
        this.contentlink = contentlink;
    }
}
