package com.cky.springbootmultithreading.repository;

import com.cky.springbootmultithreading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
