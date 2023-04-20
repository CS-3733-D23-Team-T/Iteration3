package edu.wpi.tacticaltritons.database;

public enum RequestStatus {
    BLANK(""),
    PROCESSING("Processing"),
    DONE("Done");
    private String str;

    RequestStatus(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }

    public static RequestStatus getEnum(String value) {
        if (value == null || value.isEmpty()) {
            return BLANK;
        }
        for (RequestStatus r : RequestStatus.values()) {
            if (r.str.equalsIgnoreCase(value)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + RequestStatus.class.getName() + "." + value);
    }

}