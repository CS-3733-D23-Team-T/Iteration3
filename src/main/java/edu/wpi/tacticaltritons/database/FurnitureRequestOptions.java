package edu.wpi.tacticaltritons.database;

public class FurnitureRequestOptions {
    private String itemName;
    private String itemType;
    private String itemDescription;

    public FurnitureRequestOptions(String itemName, String itemType, String itemDescription) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemDescription = itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}

