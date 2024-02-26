package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "CART")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    private int cId;

    @OneToOne
    @JsonManagedReference
    private User user;

    @OneToOne
    @JsonManagedReference
    private Address address;

    private int totalCost;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ItemQuantity> itemQuantity;


}
