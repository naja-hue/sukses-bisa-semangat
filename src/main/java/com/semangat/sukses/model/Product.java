package com.semangat.sukses.model;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @ManyToOne
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;  // Relasi dengan Admin

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stock")
    private int stock;


    // Konstruktor dengan parameter
    public Product(Long id, Admin admin, String name, double price, String description, int stock) {
        this.id = id;
        this.admin = admin;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }


    public Product() {

    }



    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    // Getter dan Setter untuk description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter dan Setter untuk price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter dan Setter untuk id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter dan Setter untuk name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter dan Setter untuk stock
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
