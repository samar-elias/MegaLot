package com.hudhud.megalot.AppUtils;

import com.hudhud.megalot.AppUtils.Responses.Draw;
import com.hudhud.megalot.AppUtils.Responses.NextDraw;
import com.hudhud.megalot.AppUtils.Responses.User;

import java.util.Locale;

public class AppDefs {
    //Shared Preferences
    public static final String SHARED_PREF_KEY = "SHARED";
    public static final String ID_KEY = "ID";
    public static final String NAME_KEY = "NAME";
    public static final String PHONE_KEY = "PHONE";
    public static final String EMAIL_KEY = "EMAIL";
    public static final String IMAGE_KEY = "IMAGE";
    public static final String BIRTH_DATE_KEY = "BIRTH_DATE";
    public static final String COUNTRY_KEY = "COUNTRY";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final String TOKEN_KEY = "TOKEN";
    public static final String FCM_TOKEN_KEY = "FCM_TOKEN";
    public static final String TOKEN_TYPE_KEY = "TOKEN_TYPE";
    public static final String FB_ID_KEY = "FB_ID";
    public static final String GOOGLE_ID_KEY = "GO_ID";
    public static final String STATUS_KEY = "STATUS";

    public static final String LANGUAGE_KEY = "LANGUAGE";

    public static User user = new User();
    public static String language = getLanguage();
    public static NextDraw nextDraw = new NextDraw();
    public static double totalIncreasedValue = 0.00;

    public static String getLanguage(){
        if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
            return "ar";
        }else {
            return "en";
        }
    }
}
