package edu.wpi.tacticaltritons.database;

public class SupplyRequestOptions {
    private String itemName;
    private double price;
    private String shop;
    private String shopDescription;
    private String itemType;
    private String itemDescription;

    public SupplyRequestOptions(String itemName, double price, String shop, String itemType, String itemDescription) {
        this.itemName = itemName;
        this.price = price;
        this.shop = shop;
        this.itemType = itemType;
        this.itemDescription = itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
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

