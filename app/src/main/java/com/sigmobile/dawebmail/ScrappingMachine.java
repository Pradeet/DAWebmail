package com.sigmobile.dawebmail;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.HttpRequest;
import com.jaunt.NotFound;
import com.jaunt.UserAgent;
import com.jaunt.component.Form;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;

import com.sigmobile.services.SavedStatistics;
import com.sigmobile.tools.Printer;
import com.sigmobile.utils.Constants;

public class ScrappingMachine {
    public static UserAgent userAgent = new UserAgent();
    public static ArrayList<EmailMessage> allemails = new ArrayList<>();
    public static String homepage_link = "";
    public static Boolean readAgain = false;
    public static Boolean firstrun_resendvar = false;
    public static int totalnew = 0;

    public Time lastloggedin;

    private String username, pwd;
    Context context;

    public ScrappingMachine(String un, String pw, Context con) {
        this.username = un;
        this.pwd = pw;
        this.context = con;
    }

    public String logIn(String username, String pwd) {
        try {

            userAgent = new UserAgent();
            Printer.println("Attempting login with " + username + " " + pwd);
            userAgent.settings.checkSSLCerts = false;
            userAgent.visit("https://webmail.daiict.ac.in/zimbra/");

            Form form = userAgent.doc.getForm("<form name=loginForm>");
            form.setTextField("username", username);
            form.setPassword("password", pwd);
            form.submit("Log In");

            userAgent.visit("https://webmail.daiict.ac.in/zimbra/h/search?mesg=welcome&initial=true&app=");

            Printer.println("location: " + userAgent.getLocation());
            homepage_link = userAgent.getLocation();
            Printer.println("Read again - " + readAgain);
            if (readAgain) {
                readAgain = false;
                scrapeAllMessagesfromInbox(firstrun_resendvar);
                Printer.println("Checking mails now.");
            }
            String title = userAgent.doc.findFirst("<title>").getText();
            Printer.println(title);
            if (title.equals("DA-IICT Webmail Log In")
                    || userAgent.getLocation().trim()
                    .equals("https://webmail.daiict.ac.in/zimbra/")) {
                // BackgroundService.isLoggedin = false;
                return "login unsuccessful";
            }
            Printer.println("Login Successful");
            Constants.isLoggedin = true;

            return "login successful";
        } catch (Exception e) {
            Printer.println("Error while logging in - " + e.getMessage());
        }
        Printer.println("Login Unsuccessful");

        return "login unsuccessful";
    }

    public void scrapeAllMessagesfromInbox(Boolean firstrun) {
        // go to the homepage
        totalnew = 0;
        try {
            // if not logged in, login first.
            if (homepage_link.equals("")) {
                Printer.println("Not Logged in.");
                Printer.println("Logging in from scrapeallmsgs.");
                logIn(username, pwd);
                firstrun_resendvar = firstrun;
                readAgain = true;
                Printer.println("read again -" + readAgain);
                return;
            }
            userAgent.visit("" + homepage_link);
        } catch (Exception e) {
            System.out.println("Error visiting homepage, method - scrapeAllMessagesfromInbox = " + e.getMessage());
        }
        Printer.println("Starting to scrap from scrapeallmsgs");
        Printer.println("Firstrun is - " + firstrun);
        Boolean cont = scrapeMessagesfromPage(firstrun);
        Printer.println("Go to next page? - " + cont);
        if (cont)
            while (goNextPage()) {
                scrapeMessagesfromPage(firstrun);
            }
        // done refreshing
        try {
            Calendar calendar = Calendar.getInstance();
            String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
            String date = calendar.get(Calendar.DAY_OF_MONTH) + " / " + (calendar.get(Calendar.MONTH) + 1);
            SavedStatistics lastrefstat = new SavedStatistics(time + " | " + date);
            lastrefstat.save();
            Printer.println("Saved record - " + time + " | " + date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean scrapeMessagesfromPage(Boolean firstrun) {
        Printer.println("Firstrun in 25inbox - " + firstrun);
        Boolean cont = true;
        int counter_toexecutemasterrefresh = 0;
        Element anchormsglist = userAgent.doc.findEvery("<tbody>");

        Printer.println("Alright. Just before anchrmsglist");

        Printer.println(userAgent.getLocation());

        for (Element anchoreachmsg : anchormsglist.findEvery("<tr>")) {
            // now i have a list of each_msg with their ind tag within.
            // Printer.println("\nMSG\n"+anchoreachmsg.innerHTML());
            // to get senders name, subject, i have to use these inner HTML
            // tags

            Printer.println("Entered anchoreacchmsg");

            try {
                Element fromtag = null;
                Element datetag = null;
                Element subtag = null;
                String contentlink = "";
                String rur = "";
                String fromname = "";
                if (anchoreachmsg.findEach("<td>").size() == 5) {
                    fromtag = anchoreachmsg.findEach("<td>").getElement(2);
                    datetag = anchoreachmsg.findEach("<td>").getElement(3);
                    subtag = anchoreachmsg.findFirst("<span>");
                    contentlink = anchoreachmsg.findFirst("<a href>").getAt("href");
                    contentlink = convertLink(contentlink);
                    rur = anchoreachmsg.findFirst("<img>").getAt("title").trim();
                    fromname = "" + fromtag.getText().trim();
                } else {
                    fromtag = anchoreachmsg.findEach("<td>").getElement(1);
                    datetag = anchoreachmsg.findEach("<td>").getElement(2);
                    subtag = anchoreachmsg.findFirst("<span>");
                    contentlink = anchoreachmsg.findFirst("<a href>").getAt("href");
                    contentlink = convertLink(contentlink);
                    rur = anchoreachmsg.findFirst("<img>").getAt("title").trim();
                    fromname = "" + fromtag.getText().trim();
                    fromname = fromname.substring(0, fromname.length() - 12);
                }

                // if (fromname.contains("@daiict.ac"))
                // fromname.replace("@daiict.ac.in", "");
                String sub = "" + subtag.getText().trim();
                sub = convertText(sub);
                String d = "" + datetag.getText().trim().replace("&nbsp;", "").replace("\n", "");
                String hasatt = "isempty";
                if (anchoreachmsg.innerHTML().contains("title='Attachment'")) {
                    hasatt = "notempty";
                }
                Printer.println("hasatt = " + hasatt);

                // HAVE TO CHANGE THIS!
                // alright so the problem is if I save it here, there is too
                // much work being done in this async task. hence, Ive
                // created a
                // temp arraylis to store all emails in this thread and
                // later in
                // the main thread run the save operations.
                // WARNING : FOR FUTURE
                // This too must be changed for better functionality and
                // efficiency.

                // Printer.println("checking email");

                // the refresh method- it looks at the top of the db. Once it
                // finds the same email while scrapping, it breaks from the
                // loop.
                // int lastIndex = (int) SugarRecord.count(EmailMessage.class);

                // WARNING THIS MUST BE CHANGED LATER => (For refresh)
                Printer.println("sub - " + sub);

                if (!firstrun) {
                    EmailMessage lastemail_in_db = EmailMessage.listAll(
                            EmailMessage.class).get(
                            (int) (SugarRecord.count(EmailMessage.class, null, null) - 1));
                    if (fromname.equals(lastemail_in_db.fromname) && sub.equals(lastemail_in_db.subject)) {
                        Printer.println("Found same email!");
                        cont = false;
                        break;
                    } else
                        counter_toexecutemasterrefresh++;
                }
                allemails.add(new EmailMessage(fromname, "isempty", sub,
                        "isempty", d, "isempty", rur, contentlink, "isempty",
                        hasatt, "isempty", "isempty", "isempty", "isempty",
                        "isempty"));

                // fetchEmailContent(allemails.get(allemails.size()-1));

                totalnew++;
                // if (totalnew > 100) {
                // return false;
                // }

            } catch (Exception e) {
                Printer.println("Error in scrapeAllMessagesfromPage"
                        + e.getMessage());
            }
        }
        if (counter_toexecutemasterrefresh >= 15) {
            masterRefresh();
            counter_toexecutemasterrefresh = 0;
            cont = false;
        }
        return cont;
    }

    public boolean goNextPage() {
        String div_yesnext = "nonextpageman";
        try {
            Elements npimgs = userAgent.doc.findEvery("<img>");
            for (Element img : npimgs) {
                try {
                    if (img.getAt("title").equals("go to next page")) {
                        div_yesnext = "true";
                        break;
                    }
                } catch (NotFound e) {
                    Printer.println("Error in code - " + e.getMessage());
                }
            }
            Printer.println("Found title - " + div_yesnext);

            if (div_yesnext.equals("true")) {
                String urltonext = userAgent.doc.findFirst("<a id='NEXT_PAGE'>").getAt("href");
                userAgent.visit(convertLink(urltonext));
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Printer.println("Error in code " + e.getMessage());
        }
        return false;
    }

    static public void clear_AllEmailsAL() {
        allemails.clear();
    }

    public String convertLink(String link) {
        link = link.replaceAll("&#039;", "'");
        link = link.replaceAll("amp;", "");
        return link;
    }

    public static String convertText(String text) {
        text = text.replaceAll("&#039;", "'");
        text = text.replaceAll("amp;", "");
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        text = text.replaceAll("&#034;", "\"");
        return text;
    }

    static public Boolean checkIfLoggedIn() {

        // the quicker method that checks if I logged in before and how long it
        // has been since last logged in
        Printer.println("HOMEPAGE LINK - " + homepage_link);
        if (homepage_link.equals("")
                || homepage_link.trim().equals(
                "https://webmail.daiict.ac.in/zimbra/")) {
            return false;
        }
        return true;
    }

    public static Boolean checkifLoggedInLong() {
        // the method that takes time -> goes to the website and sees the title.
        try {
            Printer.println("The homepage link is - " + homepage_link);
            userAgent
                    .visit(""
                            + "https://webmail.daiict.ac.in/zimbra/h/search?mesg=welcome&initial=true&app=");
        } catch (Exception e) {
            Printer.println("error in checkifloggedin " + e.getMessage());
        }
        String title = "DA-IICT Webmail Log In";
        try {
            // im facing a problem here, in the background, it keeps finding
            // a
            // nullpointer exception.
            // so :
            if (userAgent.doc.findFirst("<title>").getText().trim() != null) {
                title = userAgent.doc.findFirst("<title>").getText().trim();
            }

            if (title.equals("DA-IICT Webmail Log In")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            Printer.println("Error in checkifloggedinlong- "
                    + e.getMessage());
        }
        return false;
    }

    public void fetchEmailContent(EmailMessage email) {

        Printer.println("-------ALL CONTENT------");
        String link = email.contentlink;

        try {
            Printer.println("connecting to link");
            userAgent.visit(link);
            String resp = userAgent.doc.innerHTML();
            int totalattlinks = 0;

            // for the link to the att
            Elements attlinks = userAgent.doc.findEvery("<a>");
            for (Element attlink : attlinks) {
                if (attlink.innerText().equals("Download")) {
                    totalattlinks++;
                    Printer.println("HAS LINK");
                    String finallink = convertLink(attlink.getAt("href"));
                    Printer.println("download link = " + finallink);
                    if (totalattlinks == 1)
                        email.attlink1 = finallink;
                    if (totalattlinks == 2)
                        email.attlink2 = finallink;
                    if (totalattlinks == 3)
                        email.attlink3 = finallink;
                }
            }

            // // for the att name
            // Elements atts = userAgent.doc.findEvery("<img>");
            // for (Element att : atts) {
            // if (att.outerHTML().contains("???application")) {
            // String attname = att.getAt("alt");
            // }
            // }

            String emailtext = "";
            if (resp.contains("noscript")) {
                if (resp.contains("doc.write(")) {
                    emailtext = resp.substring(resp.indexOf("doc.write(") + 10,
                            resp.indexOf("doc.close()") - 4);
                    emailtext = emailtext.substring(1);
                    emailtext = emailtext.substring(0, emailtext.length() - 2);

                    emailtext = resp.substring(resp.indexOf("doc.write(") + 10,
                            resp.indexOf("doc.close()") - 4);

                    emailtext = emailtext.substring(1);
                    emailtext = emailtext.substring(0, emailtext.length() - 2);

                    emailtext = emailtext.replaceAll("\\\\u003C", "<");
                    emailtext = emailtext.replaceAll("\\\\u003E", ">");

                } else
                    emailtext = "This webmail contains pdf/webpage content.\nText for this format of the webmail is currrently unavailable.";
            } else {
                emailtext = resp.substring(resp.indexOf("iframeBody"));
                emailtext = emailtext.substring(emailtext.indexOf(">") + 1, emailtext.indexOf("</td>"));
            }

            char emailchars[] = emailtext.toCharArray();

            for (int i = 0; i < emailchars.length - 1; i++)
                if (emailchars[i] == '\\' && emailchars[i + 1] == 'n') {
                    emailchars[i] = '\0';
                    emailchars[i + 1] = '\0';
                }

            emailtext = String.copyValueOf(emailchars);

            Printer.println("emailtext = " + emailtext);

            email.content = emailtext;
            email.readunread = "Read Message";

            Elements msghdrvalues = userAgent.doc
                    .findEvery("<td class='MsgHdrValue'>");

            String address = msghdrvalues.getElement(0).getText().toString()
                    .trim().replaceAll("&gt;", "");
            address = address.substring(address.indexOf(";") + 1);
            String subject = msghdrvalues.getElement(1).getText().toString()
                    .trim();
            subject = convertText(subject);

            String datefull = userAgent.doc
                    .findFirst("<td class='MsgHdrSent'>").getText().toString()
                    .trim();

            datefull = datefull.substring(datefull.indexOf(",") + 2);

            email.fromaddress = address;
            email.subjectfull = subject;
            email.dateentire = datefull;
            email.save();

        } catch (Exception e) {
            Printer.println("Error in fetchemailcontent" + e.getMessage());
        }
    }

    public void getValues_forDelete(ArrayList<EmailMessage> emails_tobedeleted) {
        // cloning into this arraylist so that the webmials are deleted from the
        // app only once the request is done on the website
        ArrayList<EmailMessage> emails_tobedeleted_clone = new ArrayList<EmailMessage>();
        for (EmailMessage email : emails_tobedeleted)
            emails_tobedeleted_clone.add(email);

        try {
            ArrayList<String> values_checkboxes = new ArrayList<String>();

            if (!checkifLoggedInLong()) {
                Printer.println("Not logged in man");
                // REMEMBER THIS WONT WORK OFFLINE. SO CHANGE THIS LATER

                SharedPreferences pref = context.getSharedPreferences(
                        Constants.USER_PREFERENCES, Context.MODE_PRIVATE);

                Printer.println("username - "
                        + pref.getString("Username", "none"));
                logIn(pref.getString("Username", "none"),
                        pref.getString("Password", "none"));
                Printer.println("So logged you in");
            }

            userAgent.visit(homepage_link); // for a refresh so that im sure
            // were deleting the correct emails

            Printer.println("Reached here mate");

            Boolean cont = true;
            while (cont) {
                Element anchormsglist = userAgent.doc.findEvery("<tbody>");
                for (Element anchoreachmsg : anchormsglist.findEvery("<tr>")) {
                    try {
                        Element fromtag = anchoreachmsg.findEach("<td>")
                                .getElement(2);
                        Element subtag = anchoreachmsg.findFirst("<span>");
                        Element chckbox = anchoreachmsg.findFirst("<input>");

                        String globalvalue = chckbox.getAt("value");

                        System.out
                                .println("from - " + fromtag.getText().trim());
                        Printer.println("sub - " + subtag.getText().trim());

                        for (int i = 0; i < emails_tobedeleted.size(); i++) {
                            if (fromtag.getText().toString().trim()
                                    .equals(emails_tobedeleted.get(i).fromname.trim())
                                    && subtag.getText().toString().trim()
                                    .equals(emails_tobedeleted.get(i).subject
                                            .trim())) {
                                System.out.println("Found!\nEmail to be deleted "
                                        + emails_tobedeleted.get(i).fromname
                                        + " " + globalvalue);
                                values_checkboxes.add(globalvalue);
                                emails_tobedeleted.remove(i);
                                i--;
                            }
                        }

                        if (emails_tobedeleted.size() == 0)
                            break;
                    } catch (Exception e) {
                        Printer.println("Error in code " + e.getMessage());
                    }

                }
                Printer.println("\n\n---\n\nEmailleft = "
                        + emails_tobedeleted.size());
                for (EmailMessage email : emails_tobedeleted)
                    Printer.println(email.fromname + email.subject);
                if (emails_tobedeleted.size() != 0) {
                    if (goNextPage() == false) {
                        System.out
                                .println(emails_tobedeleted.size()
                                        + " webmails that are already deleted but exist here");
                        for (EmailMessage email : emails_tobedeleted) {
                            Printer.println(email.getId() + " "
                                    + email.fromname + " " + email.subject);
                            EmailMessage.findById(EmailMessage.class,
                                    email.getId()).delete();
                            Printer.println("--Deleted--");
                        }
                        cont = false;
                        break;
                    }
                } else {
                    cont = false;
                    break;
                }
            }
            Printer.println("sending values to delete function");
            deleteMails(values_checkboxes, emails_tobedeleted_clone);

        } catch (Exception e) {
            Printer.println(e.getMessage());
        }
    }

    public void deleteMails(ArrayList<String> values_checkboxes,
                            ArrayList<EmailMessage> emails_tobedeleted) {
        Printer.println("deleting");
        try {
            // for this I will have to refresh the page, get the 'value' tags
            // for each email, and delete those emails that he has selected. so
            // basically here, i will fetch the values in an arraylist as
            // argument, and in another function fetch the corresponding value
            // tags!

            Form form = userAgent.doc.getForm(1);
            HttpRequest request = form.getRequest("Delete");
            for (String value : values_checkboxes) {
                request.addNameValuePair("id", "" + value);
                Printer.println("Value " + value + " added");
            }
            userAgent.send(request);
            Printer.println("Request done on internet, now on app ");
            for (EmailMessage email : emails_tobedeleted) {
                Printer.println(email.getId() + " " + email.fromname + " "
                        + email.subject);
                EmailMessage.findById(EmailMessage.class, email.getId())
                        .delete();
                Printer.println("--Deleted--");
            }
            Printer.println("All Deleted");
            // Printer.println(userAgent.getLocation());
            // Form form = userAgent.doc.getForm(1);
            // HttpRequest request = form.getRequest("Delete");
            // request.addNameValuePair("id", "" + values_checkboxes.get(0));
            // form.setCheckBox("id", true);
            // userAgent.send(request);

        } catch (Exception e) {
            Printer.println("Error in deleting- " + e.getMessage());
        }
    }

    public void masterRefresh() {
        Printer.println("is callin master refresh");
        EmailMessage.deleteAll(EmailMessage.class);
        Printer.println("deleted. now, what is remaining in database -");
        Printer.println(EmailMessage.count(EmailMessage.class, null, null) + "");
        Printer.println();
    }

    static public String convertContent(String doc) {
        while (doc.indexOf("\\u003C") != -1) {
            int index = doc.indexOf("\\u003C");
            doc += doc.substring(0, index) + "<" + doc.substring(index + 6);
        }
        while (doc.indexOf("\\u003E") != -1) {
            int index = doc.indexOf("\\u003E");
            doc += doc.substring(0, index) + "<" + doc.substring(index + 6);
        }
        return doc;
    }

    public void scrapeAllMessagesfromSentbox() {

    }
}
