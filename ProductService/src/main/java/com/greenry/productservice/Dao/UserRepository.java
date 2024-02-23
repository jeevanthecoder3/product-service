package com.greenry.productservice.Dao;

import com.greenry.productservice.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findUserByUserName(String userName);
}
