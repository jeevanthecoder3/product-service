package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Table
@Entity(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {

    @Id
    @GeneratedValue
    private int Sid;
    @Column(unique = true)
    private String sellerName;
    private String sellerPhone;
    private String sellerEmail;
    private int NoOfProducts;

    @OneToOne
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Product> product;

}
