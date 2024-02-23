package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Table
@Entity(name = "PRODUCT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private int PId;

    @Column(unique = true)
    private String PTitle;

    private String category;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Features> features;

    private String pImageURL;

    private int pPrice;
    private String pDescription;
    private String image;
    private int quantity;

    @ManyToOne
    @JsonBackReference
    private Seller seller;


    @OneToMany
    @JsonManagedReference
    private Set<ItemQuantity> itemQuantity;
}
