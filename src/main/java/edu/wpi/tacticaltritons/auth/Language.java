package edu.wpi.tacticaltritons.auth;

import java.util.Locale;

public enum Language {
    English("en"),
    Spanish("es"),
    Chinese("zh"),
    Korean("ko"),
    Hindi("hi"),
    Greek("el"),
    German("de");
    private final String lang;
    Language(String lang){
        this.lang = lang;
    }
    public String getLanguage(){
        return lang;
    }
    public static Language parseLanguage(String language){
        if(language == null) return null;
        if(English.name().equals(language)) return English;
        else if(Spanish.name().equals(language)) return Spanish;
        else if(Chinese.name().equals(language)) return Chinese;
        else if(Korean.name().equals(language)) return Korean;
        else if(Hindi.name().equals(language)) return Hindi;
        else if(Greek.name().equals(language)) return Greek;
        else if(German.name().equals(language)) return German;

        return null;
    }
    public String formalName(){
        return this.name();
    }
}
