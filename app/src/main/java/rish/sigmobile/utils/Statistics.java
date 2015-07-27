package rish.sigmobile.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rish.sigmobile.dawebmail.EmailMessage;
import rish.sigmobile.tools.Printer;

public class Statistics {

    ArrayList<EmailMessage> allemails;

    int totalEmails = 0, totalEmailsfromdiffererentsenders = 0;
    public ArrayList<hashSender_Number> map_;

    public Statistics() {
        map_ = new ArrayList<>();
        fetchEmailsfromDB();
        calculateStatistics();
        displayStats();
    }

    public void fetchEmailsfromDB() {
        allemails = (ArrayList<EmailMessage>) EmailMessage
                .listAll(EmailMessage.class);
    }

    public void calculateStatistics() {
        totalEmails = allemails.size();
        Collections.sort(allemails, new CustomComparator());

        // to set values (number) to each sender name
        if (totalEmails > 10) {
            String temp_sender = allemails.get(0).fromname;
            int counter = 1;

            for (EmailMessage email : allemails) {
                if (email.fromname.equals(temp_sender)) {
                    counter++;
                } else {
                    map_.add(new hashSender_Number(temp_sender, counter));
                    temp_sender = email.fromname;
                    counter = 1;
                }
            }
        } else {
            map_.add(new hashSender_Number("NO WEBMAILS", 0));
            map_.add(new hashSender_Number("NO WEBMAILS", 0));
            map_.add(new hashSender_Number("NO WEBMAILS", 0));
        }
    }

    public void displayStats() {
        Collections.sort(map_, new CustomComparator_number());

        // for some reason sysadmin emails arent being detected :/
        Printer.println("--------STATISTICS--------");
        Printer.println("Total " + totalEmails + " ");
        int t = 0;
        for (hashSender_Number map_one : map_) {
            System.out.println(map_one.getSender() + " " + map_one.getNumber());
            if (t++ > 3)
                break;
        }

    }

    public class CustomComparator implements Comparator<EmailMessage> {
        @Override
        public int compare(EmailMessage o1, EmailMessage o2) {
            return o1.fromname.compareTo(o2.fromname);
        }
    }

    public class CustomComparator_number implements
            Comparator<hashSender_Number> {
        @Override
        public int compare(hashSender_Number o1, hashSender_Number o2) {
            return o2.getNumber() - o1.getNumber();
        }
    }

    public class hashSender_Number {
        public String sender;
        public int number;

        public hashSender_Number(String s, int i) {
            sender = s;
            number = i;
        }

        public int getNumber() {
            return number;
        }

        public String getSender() {
            return sender;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }
    }

    public ArrayList<hashSender_Number> getArrayList() {
        return map_;
    }

}