package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Table
@Entity(name = "PRODUCT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"seller","itemQuantity"})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int PId;

    @Column(unique = true)
    private String PTitle;

    private String category;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference("product-features")
    private Set<Features> features;

    private String pImageURL;

    private int pPrice;
    private String pDescription;
    private String image;
    private int quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<ItemQuantity> itemQuantity;

    @ManyToOne
    @JsonBackReference("product-seller")
    private Seller seller;

}
