package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue
    private int Qid;

    private int quantity;
    @OneToOne
    private Product product;

    @ManyToOne
    @JsonBackReference
    private Cart cart;
}
