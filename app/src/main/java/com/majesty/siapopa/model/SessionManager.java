package com.majesty.siapopa.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //Variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    //Session names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMMBERME = "rememberMe";
    public static final String SESSION_ID = "idDetail";

    //User session variables
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_PROVINSI = "provinsi";
    public static final String KEY_KABUPATEN = "kabupaten";
    public static final String KEY_IDUSERGRP = "idUserGroup";
    public static final String KEY_NAMA = "nama";

    //Remember Me variables
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONPHONENUMBER = "phoneNumber";
    public static final String KEY_SESSIONPASSWORD = "password";

    public static final String KEY_IDHASIL = "id_hasil";
    public static final String KEY_DATE = "date";

    //Constructor
    public SessionManager(Context _context, String sessionName) {
        context = _context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    /*
    Users
    Login Session
     */

    public void createLoginSession(String username, String alamat, String provinsi, String kabupaten, String idUserGroup, String nama) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_PROVINSI, provinsi);
        editor.putString(KEY_KABUPATEN, kabupaten);
        editor.putString(KEY_IDUSERGRP, idUserGroup);
        editor.putString(KEY_NAMA, nama);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_ALAMAT, usersSession.getString(KEY_ALAMAT, null));
        userData.put(KEY_PROVINSI, usersSession.getString(KEY_PROVINSI, null));
        userData.put(KEY_KABUPATEN, usersSession.getString(KEY_KABUPATEN, null));
        userData.put(KEY_IDUSERGRP, usersSession.getString(KEY_IDUSERGRP, null));
        userData.put(KEY_NAMA, usersSession.getString(KEY_NAMA, null));

        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else
            return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

    /*
    Remember Me
    Session Functions
     */

    public void createRememberMeSession(String phoneNo, String password) {

        editor.putBoolean(IS_REMEMBERME, true);
        editor.putString(KEY_SESSIONPHONENUMBER, phoneNo);
        editor.putString(KEY_SESSIONPASSWORD, password);

        editor.commit();
    }

    public void createIdHasilSession(String id_hasil, String date) {

        editor.putString(KEY_IDHASIL, id_hasil);
        editor.putString(KEY_DATE, date);

        editor.commit();
    }

    public HashMap<String, String> getRemeberMeDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_SESSIONPHONENUMBER, usersSession.getString(KEY_SESSIONPHONENUMBER, null));
        userData.put(KEY_SESSIONPASSWORD, usersSession.getString(KEY_SESSIONPASSWORD, null));

        return userData;
    }

    public HashMap<String, String> getIdHasilDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_IDHASIL, usersSession.getString(KEY_IDHASIL, null));
        userData.put(KEY_DATE, usersSession.getString(KEY_DATE, null));

        return userData;
    }

    public void clearIdHasil() {
        editor.clear();
        editor.commit();
    }

    public boolean checkRememberMe() {
        if (usersSession.getBoolean(IS_REMEMBERME, false)) {
            return true;
        } else
            return false;
    }

    public void clearRememberMeSession() {
        editor.clear();
        editor.commit();
    }


}
