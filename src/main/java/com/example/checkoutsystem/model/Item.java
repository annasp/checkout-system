package com.example.checkoutsystem.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    public Item() {

    }

    public Item(String code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
