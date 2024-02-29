package com.greenry.productservice.Dao;

import com.greenry.productservice.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface CartRepository extends JpaRepository<Cart,Integer> {


}
