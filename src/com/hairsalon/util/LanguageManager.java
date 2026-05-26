package com.hairsalon.util;

public class LanguageManager {

    private static String currentLanguage = "en";

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    public static void setLanguage(String language) {
        currentLanguage = language;
    }

    public static boolean isAlbanian() {
        return currentLanguage.equals("sq");
    }
}