package edu.wpi.tacticaltritons.database;

public class RequestOptions {
  private String itemName;
  private double price;
  private String restaurant;

  public RequestOptions(String itemName, double price, String restaurant) {
    this.itemName = itemName;
    this.price = price;
    this.restaurant = restaurant;
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
}
