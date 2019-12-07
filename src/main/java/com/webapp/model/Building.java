package com.webapp.model;

/**
 * @author Juntao Peng
 * @author Shangzhen Li
 */
public class Building {
  private int id;
  private String name;
  private String description;
  private String price;

  public Building() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public Building(int id, String name, String description, String price) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public Building(String name, String description, String price) {
    this.name = name;
    this.description = description;
    this.price = price;
  }
}
