package com.air_asia.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rama Gopal
 * Project Name - cart-microservice
 */

@Document(collection = "carts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;
    private double totalAmount;

}
