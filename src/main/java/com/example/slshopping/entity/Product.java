package com.example.slshopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * 商品情報
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 10, message = "商品名は1文字以上10文字以内で入力してください")
    @Column(nullable = false, length = 10, unique = true)
    private String name;

    @Size(min = 1, max = 50, message = "商品説明は1文字以上50文字以内で入力してください")
    @Column(nullable = false, length = 50)
    private String description;

    @NotNull(message = "価格を入力してください")
    @Min(value = 1, message = "価格は1円以上で入力してください")
    @Column(nullable = false)
    private int price;

    @Column
    private String image;

    @PositiveOrZero(message = "長さは0.0以上で入力してください")
    @Column
    private double length;

    @PositiveOrZero(message = "幅は0.0以上で入力してください")
    @Column
    private double width;

    @PositiveOrZero(message = "高さは0.0以上で入力してください")
    @Column
    private double height;

    @PositiveOrZero(message = "重さは0.0以上で入力してください")
    @Column
    private double weight;

    @NotNull(message = "カテゴリーを選択してください")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotNull(message = "ブランドを選択してください")
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public Product() {
    }

    public Product(Long id, String name, String description, int price, String image, double length, double width,
            double height, double weight, Category category, Brand brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.category = category;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getImage() {
        return "/product-images/" + id + "/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
