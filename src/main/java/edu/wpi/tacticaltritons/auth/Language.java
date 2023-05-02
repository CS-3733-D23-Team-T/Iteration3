package edu.wpi.tacticaltritons.auth;

import java.util.Locale;

public enum Language {
    English("en"),
    Spanish("es");
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
        return null;
    }
    public String formalName(){
        return this.name();
    }
}
