package com.greenry.productservice.Dao;

import com.greenry.productservice.Entities.ItemQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ItemQuantityRepository extends JpaRepository<ItemQuantity,Integer> {

}
