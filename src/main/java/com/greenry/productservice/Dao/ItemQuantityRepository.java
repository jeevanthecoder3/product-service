package com.greenry.productservice.Dao;

import com.greenry.productservice.Entities.ItemQuantity;
import com.greenry.productservice.Entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface ItemQuantityRepository extends JpaRepository<ItemQuantity,Integer> {

    public ItemQuantity findItemQuantityByProduct(Product product);

//    @Query(value = "DELETE FROM ITEMQUANTITY H WHERE H.qid=:ID ;",nativeQuery = true)

    @Transactional
    public void deleteItemQuantityByQid(int qid);
}
