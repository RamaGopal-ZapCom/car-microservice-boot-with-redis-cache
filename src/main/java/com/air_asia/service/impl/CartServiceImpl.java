package com.air_asia.service.impl;

import com.air_asia.repository.CartRepository;
import com.air_asia.schema.Cart;
import com.air_asia.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final RedisTemplate<String, Cart> redisTemplate;
    private static final String CACHE_PREFIX = "CART_";

    public CartServiceImpl(CartRepository cartRepository, RedisTemplate<String, Cart> redisTemplate) {
        this.cartRepository = cartRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Add or update cart — stores in both Redis (manual) and MongoDB.
     */
    @Override
    @CachePut(value = "cartCache", key = "#userId")
    public Cart addOrUpdateCart(String userId, Cart cart) {
        cart.setUserId(userId);

        // Check if cart already exists for user
        Optional<Cart> existingCartOpt = cartRepository.findByUserId(userId);
        if (existingCartOpt.isPresent()) {
            cart.setId(existingCartOpt.get().getId()); // set existing id to update
        }
        // Save or update in MongoDB
        Cart savedCart = cartRepository.save(cart);
        log.info(" Saved cart to MongoDB for user: {}", userId);

        // Save to Redis cache manually
        try {
            redisTemplate.opsForValue().set(CACHE_PREFIX + userId, savedCart, 10, TimeUnit.MINUTES);
            log.info(" Saved cart to Redis for user: {}", userId);
        } catch (RedisConnectionFailureException ex) {
            log.warn(" Redis connection failed, skipping Redis cache update.");
        }
        return savedCart;
    }

    /**
     * Fetch cart — first from Redis manually, fallback to Mongo.
     */
    @Override
    @Cacheable(value = "cartCache", key = "#userId")
    public Optional<Cart> getCart(String userId) {
        try {
            Cart cachedCart = redisTemplate.opsForValue().get(CACHE_PREFIX + userId);
            if (cachedCart != null) {
                log.info(" Fetched cart for user {} from Redis cache", userId);
                return Optional.of(cachedCart);
            }
            log.info(" Cart not found in Redis, fetching from MongoDB...");
        } catch (RedisConnectionFailureException ex) {
            log.warn(" Redis not available, fetching directly from MongoDB...");
        }
        return cartRepository.findByUserId(userId);
    }

    /**
     * Clear cart — delete from both Redis and MongoDB.
     */
    @Override
    @CacheEvict(value = "cartCache", key = "#userId")
    public void clearCart(String userId) {
        try {
            redisTemplate.delete(CACHE_PREFIX + userId);
            log.info("Deleted cart for user {} from Redis cache", userId);
        } catch (RedisConnectionFailureException ex) {
            log.warn(" Redis not available, unable to delete cache for {}", userId);
        }
        cartRepository.deleteByUserId(userId);
    }

    @Override
    //@Cacheable(value = "cartCache", key = "'ALL_CARTS'")
    public List<Cart> getAllCarts() {
        log.info("Fetching all carts from MongoDB");

        // Try to fetch from Redis manually first
        try {
            List<Cart> cachedCarts = (List<Cart>) redisTemplate.opsForValue().get("ALL_CARTS");
            if (cachedCarts != null) {
                log.info(" Fetched all carts from Redis cache");
                return cachedCarts;
            }
            log.info(" All carts not found in Redis, fetching from MongoDB...");
        } catch (RedisConnectionFailureException ex) {
            log.warn(" Redis not available, fetching directly from MongoDB...");
        }

        // Fetch from MongoDB
        List<Cart> allCarts = cartRepository.findAll();

        // Store in Redis manually
        try {
            //redisTemplate.opsForValue().set("ALL_CARTS", (Cart) allCarts, 10, TimeUnit.MINUTES);
            log.info(" Saved all carts to Redis cache");
        } catch (RedisConnectionFailureException ex) {
            log.warn(" Redis connection failed, skipping Redis cache update for all carts.");
        }
        return allCarts;
    }
}
