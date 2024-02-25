package com.greenry.productservice.Dao;

import com.greenry.productservice.Entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface SellerRepository extends JpaRepository<Seller,Integer> {
    public Seller findSellerBySellerName(String sellerName);
}
