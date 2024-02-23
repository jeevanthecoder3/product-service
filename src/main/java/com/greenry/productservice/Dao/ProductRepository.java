package com.greenry.productservice.Dao;

import com.greenry.productservice.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Product,Integer> {
    public Product findProductByPTitle(String pTitle);


}
