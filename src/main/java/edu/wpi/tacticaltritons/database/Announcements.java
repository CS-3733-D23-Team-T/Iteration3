package edu.wpi.tacticaltritons.database;

import java.sql.Timestamp;
import java.util.UUID;

public class Announcements {
    private final UUID id;
    private String title;
    private String content;
    private String creator;
    private Timestamp creationDate;
    private Timestamp effectiveDate;
    private String type;
    private String audience;
    private int urgency;

    public Announcements(UUID id, String title, String content, String creator, Timestamp creationDate, Timestamp effectiveDate, String type, String audience, int urgency) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.creationDate = creationDate;
        this.effectiveDate = effectiveDate;
        this.type = type;
        this.audience = audience;
        this.urgency = urgency;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }
}
