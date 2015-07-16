package rish.crearo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ColorScheme {

    static public String color_readmessage = "#F0F0F0"; // #ACACAC
    static public String color_readtextsubject = "#757575";
    static public String color_readtextsender = "#000000";
    static public String color_unreadmessage = "#E7E7E7"; // #757575
    static public String color_unreadtextsubject = "#ACACAC";
    static public String color_unreadtextsender = "#000000";

    static public String color_datetextcolor = "#0099CC";
    static public String color_actionbarcolor = "#FFB84D"; // changed #45C5D76
    static public String color_actionbartextcolor = "#FFFFFF";

    static public String color_presetsimple = "#FFFFFF";
    static public String color_presetclassic = "#777777";
    static public String color_presetgroovy = "#4C5D76";
    static public String color_presetuser = "#FFFFFF";

    // user vars
    static public String coloruser_readmessage = "#FFFFFF";
    static public String coloruser_readtextsubject = "#757575";
    static public String coloruser_readtextsender = "#000000";
    static public String coloruser_unreadmessage = "#757575";
    static public String coloruser_unreadtextsubject = "#ACACAC";
    static public String coloruser_unreadtextsender = "#000000";

    static public String coloruser_datetextcolor = "#0099CC";
    static public String coloruser_actionbarcolor = "#4C5D76";
    static public String coloruser_actionbartextcolor = "#FFFFFF";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public ColorScheme(Context context) {
        prefs = context.getSharedPreferences(Constants.USER_PREFERENCES,
                context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void initialColorSchemeSetter() {
        if (prefs.getString("color_actionbarcolor", "notassigned").equals(
                "notassigned")) {
            editor.putString("color_readmessage", "#F0F0F0").commit();
            editor.putString("color_readtextsubject", "#757575").commit();
            editor.putString("color_readtextsender", "#000000").commit();
            editor.putString("color_unreadmessage", "#E7E7E7").commit();
            editor.putString("color_unreadtextsubject", "#ACACAC").commit();
            editor.putString("color_unreadtextsender", "#000000").commit();
            editor.putString("color_datetextcolor", "#0099CC").commit();
            editor.putString("color_actionbarcolor", "#FFB84D").commit();
            editor.putString("color_actionbartextcolor", "#FFFFFF").commit();
        }
    }

    public void editColorScheme(String which, String color) {
        System.out.println("Changing color in editor - " + which + " to "
                + color);
        editor.putString(which, color).commit();
        changeColorScheme();
    }

    public void changeColorScheme() {

        color_readmessage = prefs.getString("color_readmessage", "#FFFFFF");
        color_readtextsubject = prefs.getString("color_readtextsubject",
                "#FFFFFF");
        color_readtextsender = prefs.getString("color_readtextsender",
                "#FFFFFF");
        color_unreadmessage = prefs.getString("color_unreadmessage", "#FFFFFF");
        color_unreadtextsubject = prefs.getString("color_unreadtextsubject",
                "#FFFFFF");
        color_unreadtextsender = prefs.getString("color_unreadtextsender",
                "#FFFFFF");
        color_datetextcolor = prefs.getString("color_datetextcolor", "#FFFFFF");
        color_actionbarcolor = prefs.getString("color_actionbarcolor",
                "#FFFFFF");
        color_actionbartextcolor = prefs.getString("color_actionbartextcolor",
                "#FFFFFF");

    }

    public void printColorScheme() {

        System.out.println("Colors - ");
        System.out.println(color_actionbarcolor);
        System.out.println(color_actionbartextcolor);
        System.out.println(color_datetextcolor);
        System.out.println(color_readmessage);
        System.out.println(color_readtextsender);
        System.out.println(color_readtextsubject);
        System.out.println(color_unreadmessage);
        System.out.println(color_unreadtextsender);
        System.out.println(color_unreadtextsubject);

        System.out.println("Saved colors - ");
        System.out.println(prefs.getString("color_actionbarcolor", "Default"));

    }

    public void groovyColorPreset() {
        editor.putString("color_readmessage", "#F0F0F0").commit();
        editor.putString("color_readtextsubject", "#757575").commit();
        editor.putString("color_readtextsender", "#000000").commit();
        editor.putString("color_unreadmessage", "#E7E7E7").commit();
        editor.putString("color_unreadtextsubject", "#757575").commit();
        editor.putString("color_unreadtextsender", "#000000").commit();
        editor.putString("color_datetextcolor", "#0099CC").commit();
        editor.putString("color_actionbarcolor", "#00A99D") // 299ABD
                .commit();
        editor.putString("color_actionbartextcolor", "#FFFFFF").commit();

        changeColorScheme();
    }

    public void simpleColorPreset() {
        editor.putString("color_readmessage", "#777777").commit();
        editor.putString("color_readtextsubject", "#FFFFFF").commit();
        editor.putString("color_readtextsender", "#FFFFFF").commit();
        editor.putString("color_unreadmessage", "#E7E7E7").commit();
        editor.putString("color_unreadtextsubject", "#757575").commit();
        editor.putString("color_unreadtextsender", "#757575").commit();
        editor.putString("color_datetextcolor", "#FFFFFF").commit();
        editor.putString("color_actionbarcolor", "#444444").commit();
        editor.putString("color_actionbartextcolor", "#FFFFFF").commit();

        changeColorScheme();
    }

    public void classicColorPreset() {

        editor.putString("color_readmessage", "#F0F0F0").commit();
        editor.putString("color_readtextsubject", "#222222").commit();
        editor.putString("color_readtextsender", "#000000").commit();
        editor.putString("color_unreadmessage", "#E7E7E7").commit();
        editor.putString("color_unreadtextsubject", "#222222").commit();
        editor.putString("color_unreadtextsender", "#000000").commit();
        editor.putString("color_datetextcolor", "#0099CC").commit();
        editor.putString("color_actionbarcolor", "#FFB84D") // 299ABD
                .commit();
        editor.putString("color_actionbartextcolor", "#FFFFFF").commit();

        changeColorScheme();
    }

    public void userColorPreset() {

        color_readmessage = prefs.getString("coloruser_readmessage", "#FFFFFF");
        color_readtextsubject = prefs.getString("coloruser_readtextsubject",
                "#FFFFFF");
        color_readtextsender = prefs.getString("coloruser_readtextsender",
                "#FFFFFF");
        color_unreadmessage = prefs.getString("coloruser_unreadmessage",
                "#FFFFFF");
        color_unreadtextsubject = prefs.getString(
                "coloruser_unreadtextsubject", "#FFFFFF");
        color_unreadtextsender = prefs.getString("coloruser_unreadtextsender",
                "#FFFFFF");
        color_datetextcolor = prefs.getString("coloruser_datetextcolor",
                "#FFFFFF");
        color_actionbarcolor = prefs.getString("coloruser_actionbarcolor",
                "#FFFFFF");
        color_actionbartextcolor = prefs.getString(
                "coloruser_actionbartextcolor", "#FFFFFF");
    }

    public void saveUserColor() {
        editor.putString("coloruser_readmessage", color_readmessage).commit();
        editor.putString("coloruser_readtextsubject", color_readtextsubject)
                .commit();
        editor.putString("coloruser_readtextsender", color_readtextsender)
                .commit();
        editor.putString("coloruser_unreadmessage", color_unreadmessage)
                .commit();
        editor.putString("coloruser_unreadtextsubject", color_readtextsubject)
                .commit();
        editor.putString("coloruser_unreadtextsender", color_readtextsender)
                .commit();
        editor.putString("coloruser_datetextcolor", coloruser_datetextcolor)
                .commit();
        editor.putString("coloruser_actionbarcolor", coloruser_actionbarcolor)
                .commit();
        editor.putString("coloruser_actionbartextcolor",
                coloruser_actionbartextcolor).commit();

        changeColorScheme();
    }
}
