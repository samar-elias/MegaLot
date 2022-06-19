package com.hudhud.megalot.AppUtils;

import com.hudhud.megalot.AppUtils.Responses.User;

import java.util.Locale;

public class AppDefs {
    public static User user = new User();
    public static String language = getLanguage();

    public static String getLanguage(){
        if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
            return "ar";
        }else {
            return "en";
        }
    }
}
