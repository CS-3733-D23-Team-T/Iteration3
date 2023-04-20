package edu.wpi.tacticaltritons.auth;

import com.google.errorprone.annotations.ForOverride;

import java.util.Locale;

public enum TokenTime {
    SIXTY_MINUTES(3600000),
    FORTY_FIVE_MINUTES(2700000),
    THIRTY_MINUTES(1800000),
    FIFTEEN_MINUTES(900000),
    FIVE_MINUTES(300000),
    FOREVER(-1);

    private final int tokenTime;
    TokenTime(int time){
        tokenTime = time;
    }

    public int getTokenTime() {
        return tokenTime;
    }

    public String formalName(){
        String[] parts = this.name().split("_");
        StringBuilder formalName = new StringBuilder();
        for(String part : parts){
            formalName.append(part.charAt(0))
                    .append(part.toLowerCase(Locale.ROOT).substring(1))
                    .append(" ");
        }
        return formalName.substring(0, formalName.length() - 1);
    }

    public static TokenTime parseTokenTime(int time){
        if(time == SIXTY_MINUTES.getTokenTime()) return SIXTY_MINUTES;
        else if(time == FORTY_FIVE_MINUTES.getTokenTime()) return FORTY_FIVE_MINUTES;
        else if(time == THIRTY_MINUTES.getTokenTime()) return THIRTY_MINUTES;
        else if(time == FIFTEEN_MINUTES.getTokenTime()) return FIFTEEN_MINUTES;
        else if(time == FIVE_MINUTES.getTokenTime()) return FIVE_MINUTES;
        else if(time == FOREVER.getTokenTime()) return FOREVER;
        return SIXTY_MINUTES;
    }
    public static TokenTime parseTokenTime(String time){
        if(time == null) return null;
        time = time.toUpperCase(Locale.ROOT);
        time = time.replaceAll(" ", "_");
        if(time.equals(SIXTY_MINUTES.name())) return SIXTY_MINUTES;
        else if(time.equals(FORTY_FIVE_MINUTES.name())) return FORTY_FIVE_MINUTES;
        else if(time.equals(THIRTY_MINUTES.name())) return THIRTY_MINUTES;
        else if(time.equals(FIFTEEN_MINUTES.name())) return FIFTEEN_MINUTES;
        else if(time.equals(FIVE_MINUTES.name())) return FIVE_MINUTES;
        else if(time.equals(FOREVER.name())) return FOREVER;
        return null;
    }
}
