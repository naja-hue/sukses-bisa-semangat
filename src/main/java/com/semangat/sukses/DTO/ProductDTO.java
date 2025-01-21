package com.semangat.sukses.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDTO {

    private Long id;
    private Long admin;
    private String name;
    private double price;
    private String description;
    private int stock;
    private String imageURL;

    @JsonCreator
    public ProductDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("admin") Long admin,
            @JsonProperty("name") String name,
            @JsonProperty("price") double price,
            @JsonProperty("description") String description,
            @JsonProperty("stock") int stock,
            @JsonProperty("imageURL") String imageURL) {
        this.id = id;
        this.admin = admin;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.imageURL = imageURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdmin() {
        return admin;
    }

    public void setAdmin(Long admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
