package com.air_asia.service;

import com.air_asia.schema.Cart;

import java.util.List;
import java.util.Optional;

/**
 * Created by Rama Gopal
 * Project Name - cart-microservice
 */
public interface CartService {
    Cart addOrUpdateCart(String userId, Cart cart);
    Cart getCart(String userId);
    void clearCart(String userId);
    List<Cart> getAllCarts();

}
