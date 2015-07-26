package rish.crearo.dawebmail.commands;

public interface LoginListener {

    public void onPreLogin();
    public void onPostLogin(String loginSuccess, String timeTaken);

}
