package com.air_asia.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Rama Gopal
 * Project Name - cart-microservice
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
    private String itemId;
    // flight, hotel, addon
    private String type;
    private String description;
    private double price;
}

