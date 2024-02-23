package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue
    private int Aid;

    @Column(unique = true)
    private String fullName;
    private String phone;
    private String state;
    private String city;
    private String country;
    @Column(name = "landmark")
    private String landmark;

    private String pincode;

    @Column(name = "StreetDetails")
    private String StreetDetails;

    @Column(name="HouseDetails")
    private String HouseDetails;

    @ManyToOne()
    @JsonBackReference
    private User user;
}
