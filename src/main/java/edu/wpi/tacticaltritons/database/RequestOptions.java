package edu.wpi.tacticaltritons.database;

public class RequestOptions {
  private String itemName;
  private double price;
  private String restaurant;
  private String shopDescription;
  private String itemDescription;
  private String itemType;

  public RequestOptions(String itemName, double price, String restaurant, String shopDescription, String itemDescription, String itemType) {
    this.itemName = itemName;
    this.price = price;
    this.restaurant = restaurant;
    this.shopDescription = shopDescription;
    this.itemDescription = itemDescription;
    this.itemType = itemType;
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

  public String getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(String restaurant) {
    this.restaurant = restaurant;
  }

  public String getShopDescription() {
    return shopDescription;
  }

  public void setShopDescription(String shopDescription) {
    this.shopDescription = shopDescription;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public String getItemType() {
    return itemType;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }
}
