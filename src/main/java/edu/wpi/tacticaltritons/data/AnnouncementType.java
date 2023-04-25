package edu.wpi.tacticaltritons.data;

import java.util.List;
import java.util.Locale;

public enum AnnouncementType {
    GENERAL,
    EVENT,
    HOLIDAY,
    NOTICE;
    public static List<String> collect(){
        return List.of(GENERAL.formalName(),
                    EVENT.formalName(),
                    HOLIDAY.formalName(),
                    NOTICE.formalName());
    }
    public String formalName(){
        String[] parts = this.name().split("_");
        StringBuilder builder = new StringBuilder();
        for(String part : parts){
            builder.append(part.charAt(0)).append(part.substring(1)).append(' ');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
    public static AnnouncementType parse(String announcementType){
        if(announcementType == null) return null;
        announcementType = announcementType.toUpperCase(Locale.ROOT);
        announcementType = announcementType.replaceAll("[_ ]", "");

        if(announcementType.equals(GENERAL.name())) return GENERAL;
        else if(announcementType.equals(EVENT.name())) return EVENT;
        else if(announcementType.equals(HOLIDAY.name())) return HOLIDAY;
        else if(announcementType.equals(NOTICE.name())) return NOTICE;
        return null;
    }
}
