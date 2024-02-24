package com.greenry.productservice.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Table
@Entity(name = "USER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue
    private int Uid;
    @Column(unique = true)
    private String userName;
    private String userPassword;

    @Column(unique = true)
    @Pattern(regexp = "[A-Za-z0-9._]+@[A-Za-z0-9]+\\.[A-Z|a-z]{2,}")
    private String userEmail;

    @Column(unique = true)
    @Pattern(regexp = "\\+\\{12}")
    private String userPhone;

    private boolean isUserASeller;
    private String firstName;
    private String lastName;
    private String OTP;
    private LocalDateTime otpGeneratedTime;
    private boolean isVerified=false;
    private boolean isLoggedIn=false;
    private LocalDateTime loggedInTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Address> address;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference(value="product-user")
    private Seller seller;

    @OneToOne(cascade=CascadeType.ALL)
    @JsonBackReference
    private Cart cart;


}
