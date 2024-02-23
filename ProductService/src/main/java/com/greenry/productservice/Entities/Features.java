package com.greenry.productservice.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity(name = "FEATURES")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Features {

    @Id
    @GeneratedValue
    private int fid;

    private String fName;

    @ManyToOne
    @JsonBackReference
    private Product product;
}
