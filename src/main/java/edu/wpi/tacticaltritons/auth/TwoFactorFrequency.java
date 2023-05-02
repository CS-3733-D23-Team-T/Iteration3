package edu.wpi.tacticaltritons.auth;

import java.util.Locale;

public enum TwoFactorFrequency {
    DAILY(24),
    HOURLY(1),
    EVERY(-1);
    private final int hours;
    TwoFactorFrequency(int hours){
        this.hours = hours;
    }
    public int getHours(){
        return hours;
    }
    public static TwoFactorFrequency parseFrequency(String frequency){
        if(frequency == null) return null;
        frequency = frequency.toUpperCase(Locale.ROOT);
        if(DAILY.name().equals(frequency)) return DAILY;
        else if(HOURLY.name().equals(frequency)) return HOURLY;
        else if(EVERY.name().equals(frequency)) return EVERY;
        return null;
    }
    public String formalName(){
        String formalName = this.name().toLowerCase(Locale.ROOT).substring(1);
        return this.name().charAt(0) + formalName;
    }
}

