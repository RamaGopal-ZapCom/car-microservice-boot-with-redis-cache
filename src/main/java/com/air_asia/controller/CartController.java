package com.air_asia.controller;

import com.air_asia.schema.Cart;
import com.air_asia.service.CartService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Created by Rama Gopal
 * Project Name - cart-microservice
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/add")
    public Cart addItem(@PathVariable String userId,
                        @RequestBody Cart cart) {
        return cartService.addOrUpdateCart(userId, cart);
    }

    @GetMapping("/{userId}")
    public Optional<Cart> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }

    @GetMapping("/all")
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }


}
