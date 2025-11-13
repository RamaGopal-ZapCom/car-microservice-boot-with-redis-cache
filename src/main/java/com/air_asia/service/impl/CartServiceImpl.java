package com.air_asia.service.impl;

import com.air_asia.repository.CartRepository;
import com.air_asia.schema.Cart;
import com.air_asia.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private  CartRepository cartRepository;




    /**
     * Add or update cart — stores in both Redis (manual) and MongoDB.
     */
    @Override
    @CachePut(value = "cartCache", key = "#userId")
    public Cart addOrUpdateCart(String userId, Cart cart) {
        if (userId == null || cart == null) {
            log.warn("UserId or Cart is null, cannot save to MongoDB");
            return null;
        }
        cart.setUserId(userId); // ensure consistency
        Cart savedCart = cartRepository.save(cart);
        log.info(" Saved cart to MongoDB for user: {}", userId);
        return savedCart;
    }
    @Override
    @Cacheable(value = "cartCache", key = "#userId")
    public Cart getCart(String userId) {
        if (userId == null) {
            log.warn("UserId is null — cannot fetch cart.");
            return null;
        }
        Cart cart = cartRepository.findByUserId(userId);
        log.info("Fetched cart for user: {} => {}", userId, cart != null ? "FOUND" : "NOT FOUND");
        return cart;
    }

    @Override
    @CacheEvict(value = "cartCache", key = "#userId")
    public void clearCart(String userId) {
        if (userId == null) {
            log.warn("UserId is null — cannot clear cart.");
            return;
        }
        cartRepository.deleteByUserId(userId);
        log.info("Cleared cart for user: {}", userId);
    }

    @Override
    public List<Cart> getAllCarts() {
        log.info("Fetching all carts from MongoDB");
        return cartRepository.findAll();
    }
}
