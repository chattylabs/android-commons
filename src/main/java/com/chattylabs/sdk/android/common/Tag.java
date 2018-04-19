package com.chattylabs.sdk.android.common;


public class Tag {

    private static final int CHOP_LENGTH = 22;

    public static String make(Class<?> anyClass){
        final String className = anyClass.getSimpleName();
        return CHOP_LENGTH > className.length() ?  ellipsize(className) : className.substring(0, CHOP_LENGTH) + "..";
    }

     private static String ellipsize(String str) {
        return str + new String(new char[(CHOP_LENGTH + 2) - str.length()]).replace('\0', '.');
     }
}
