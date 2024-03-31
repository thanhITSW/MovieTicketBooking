package com.example.movieticket;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class SessionManager extends AppCompatActivity {
    private static final String PREFERENCE_NAME = "MovieTicketSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_COIN = "coin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_CARD_BANK = "cardBank";
    private static final String KEY_CARD_USERNAME = "cardUsername";
    private static final String KEY_CARD_PASSWORD = "cardPassword";
    private static final String KEY_CARD_NUMBER = "cardNumber";
    private static final String KEY_CARD_DATE = "cardDate";
    private static final String KEY_IS_LOGGED_IN_CARD = "isLoggedInCard";
    private static final String KEY_PP_USERNAME = "ppUsername";
    private static final String KEY_PP_PASSWORD = "ppPassword";
    private static final String KEY_IS_LOGGED_IN_PAYPAL = "isLoggedInPayPal";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void login(String userId, String username, String email, String phone, String password, int coin) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.putInt(KEY_COIN, coin);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }
    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, null);
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }
    public int getCoin() {
        return sharedPreferences.getInt(KEY_COIN, 0);
    }

    public void minusCoins() {
        editor.putInt(KEY_COIN, getCoin() - 100);
        editor.apply();
    }

    public void bonusCoins(double price) {
        int bonusCoin = 0;
        while (price >= 100000.0) {
            price = price - 100000.0;
            bonusCoin += 10;
        }
        editor.putInt(KEY_COIN, getCoin() + bonusCoin);
        editor.apply();
    }
    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void loginCard(String cardBank, String cardUsername, String cardPassword, String cardNumber, String cardDate) {
        editor.putString(KEY_CARD_BANK, cardBank);
        editor.putString(KEY_CARD_USERNAME, cardUsername);
        editor.putString(KEY_CARD_PASSWORD, cardPassword);
        editor.putString(KEY_CARD_NUMBER, cardNumber);
        editor.putString(KEY_CARD_DATE, cardDate);
        editor.putBoolean(KEY_IS_LOGGED_IN_CARD, true);
        editor.apply();
    }
    public boolean isLoggedInCard() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN_CARD, false);
    }
    public String getCardBank() {
        return sharedPreferences.getString(KEY_CARD_BANK, null);
    }
    public String getCardUsername() {
        return sharedPreferences.getString(KEY_CARD_USERNAME, null);
    }
    public String getCardNumber() {
        return sharedPreferences.getString(KEY_CARD_NUMBER, null);
    }
    public void logoutCard() {
        editor.remove(KEY_CARD_BANK);
        editor.remove(KEY_CARD_USERNAME);
        editor.remove(KEY_CARD_PASSWORD);
        editor.remove(KEY_CARD_NUMBER);
        editor.remove(KEY_CARD_DATE);
        editor.remove(KEY_IS_LOGGED_IN_CARD);
        editor.apply();
    }

    public void loginPayPal(String ppUsername, String ppPassword) {
        editor.putString(KEY_PP_USERNAME, ppUsername);
        editor.putString(KEY_PP_PASSWORD, ppPassword);
        editor.putBoolean(KEY_IS_LOGGED_IN_PAYPAL, true);
        editor.apply();
    }
    public boolean isLoggedInPayPal() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN_PAYPAL, false);
    }
    public String getPPUsername() {
        return sharedPreferences.getString(KEY_PP_USERNAME, null);
    }
    public void logoutPayPal() {
        editor.remove(KEY_PP_USERNAME);
        editor.remove(KEY_PP_PASSWORD);
        editor.remove(KEY_IS_LOGGED_IN_PAYPAL);
        editor.apply();
    }

    public void setStatusReminderMovie(String idReminder) {
        editor.putBoolean(idReminder, true);
        editor.apply();
    }

    public boolean getStatusReminderMovie(String idReminder) {
        return sharedPreferences.getBoolean(idReminder, false);
    }
}