package edu.wpi.tacticaltritons.database;

public class Signage {
    private String title;
    private String[] forwarddir;
    private String[] leftdir;
    private String[] rightdir;
    private String[] backdir;
    private boolean singleDisplay;

    public Signage(String title, String[] forward, String[] left, String[] right, String[] back, boolean singleDisplay) {
        this.title = title;
        this.forwarddir = forward;
        this.leftdir = left;
        this.rightdir = right;
        this.backdir = back;
        this.singleDisplay = singleDisplay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getForwarddir() {
        return forwarddir;
    }

    public void setForwarddir(String[] forwarddir) {
        this.forwarddir = forwarddir;
    }

    public String[] getLeftdir() {
        return leftdir;
    }

    public void setLeftdir(String[] leftdir) {
        this.leftdir = leftdir;
    }

    public String[] getRightdir() {
        return rightdir;
    }

    public void setRightdir(String[] rightdir) {
        this.rightdir = rightdir;
    }

    public String[] getBackdir() {
        return backdir;
    }

    public void setBackdir(String[] backdir) {
        this.backdir = backdir;
    }

    public boolean isSingleDisplay() {
        return singleDisplay;
    }

    public void setSingleDisplay(boolean singleDisplay) {
        this.singleDisplay = singleDisplay;
    }
}
