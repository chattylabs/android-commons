package com.chattylabs.sdk.android.common;


public class Tag {

    private static final int CHOP_LENGTH = 22;

    public static String make(String className){
        return CHOP_LENGTH > className.length() ?  ellipsize(className) : className.substring(0, CHOP_LENGTH) + "..";
    }

    public static String make(Class<?> anyClass){
        return make(anyClass.getSimpleName());
    }

     private static String ellipsize(String str) {
        return str + new String(new char[(CHOP_LENGTH + 2) - str.length()]).replace('\0', '.');
     }
}
