package com.air_asia.repository;

import com.air_asia.schema.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by Rama Gopal
 * Project Name - cart-microservice
 */


public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
    void deleteByUserId(String userId);
}
