package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ITEMQUANTITY")
public class ItemQuantity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int qid;

    private int quantity;
    @OneToOne
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JsonBackReference
    private Cart cart;
}
