# car-microservice-boot-with-redis-cache

![img.png](img.png)

# 🚗 Car Microservice with Redis Cache

This project demonstrates a **Spring Boot microservice** integrated with **MongoDB** for data storage and **Redis** for caching — all orchestrated with **Docker Compose**.

---

## 🧱 Prerequisites

Make sure the following tools are installed on your system:

- [Docker](https://www.docker.com/products/docker-desktop/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)

---

## ⚙️ 1. Build the Spring Boot JAR

```bash
mvn clean package -DskipTests

This will generate a JAR file in the target/ directory:
target/cart-microservice.jar

🐳 2. Start All Services Using Docker Compose
docker-compose up -d --build


This command will:

Build and start the Spring Boot container

Start MongoDB and Redis containers

Create a Docker network:
car-microservice-boot-with-redis-cache_default

🧩 3. Check Running Containers

docker ps

Expected output:

CONTAINER ID   IMAGE                                                             PORTS
xxxxxxxxxxxx   car-microservice-boot-with-redis-cache-combined-cart-ms-service   0.0.0.0:8081->8081/tcp
xxxxxxxxxxxx   mongo:7                                                           0.0.0.0:27017->27017/tcp
xxxxxxxxxxxx   redis:7                                                           0.0.0.0:6379->6379/tcp


🧠 4. Connect to MongoDB Inside Docker Network

docker run -it --rm \
  --network car-microservice-boot-with-redis-cache_default \
  mongo:7 mongosh mongodb://mongodb:27017/combined_cart_db

Sample MongoDB Commands

show collections
db.carts.find().pretty()

✅ Example Output:

[
  {
    "_id": ObjectId("69157926df8fc05e0c39c07d"),
    "userId": "U10011",
    "items": [
      { "itemId": "FL1234", "type": "flight-11", "description": "Bangalore to Kuala Lumpur flight ticket", "price": 31500 },
      { "itemId": "HT4567", "type": "hotel-11", "description": "3-night stay at AirAsia partner hotel", "price": 11200 },
      { "itemId": "AD7891", "type": "addon-11", "description": "Extra baggage 15kg", "price": 1300 }
    ],
    "totalAmount": 44000
  }
]

⚡ 5. Connect to Redis Inside Docker

docker exec -it car-microservice-boot-with-redis-cache-redis-1 redis-cli

Sample Redis Commands

keys *
get cartCache::U10011

Expected output:

1) "CART_U10011"
2) "cartCache::U10011"

🌐 6. Access REST API Endpoints

| Endpoint                   | Method | URL                                           | Description                  |
| -------------------------- | ------ | --------------------------------------------- | ---------------------------- |
| `/api/cart/all`            | GET    | `http://localhost:8081/api/cart/all`          | Fetch all carts              |
| `/api/cart/add`            | POST   | `http://localhost:8081/api/cart/add`          | Add a new item to a cart     |
| `/api/cart/clear/{userId}` | DELETE | `http://localhost:8081/api/cart/clear/U10011` | Clear Redis cache for a user |


🧾 Example Request (POST /api/cart/add)


{
  "userId": "U10011",
  "items": [
    {
      "itemId": "FL1234",
      "type": "flight",
      "description": "Flight ticket Bangalore to KL",
      "price": 31500
    }
  ]
}


🧹 7. Stop and Clean Containers

To stop all running containers and remove the network:

docker-compose down

This will stop and remove:

        MongoDB container

        Redis container

        Spring Boot microservice container

        The Docker network car-microservice-boot-with-redis-cache_default


🧰 8. (Optional) Run Redis Locally (Windows)

If you want to test Redis locally:

redis-server

Then open another terminal and connect:

redis-cli
keys *


✅ Summary

| Component        | Technology              | Purpose                                |
| ---------------- | ----------------------- | -------------------------------------- |
| Backend          | Spring Boot             | REST API for Cart Service              |
| Database         | MongoDB                 | Persistent cart storage                |
| Cache            | Redis                   | Cache cart data to improve performance |
| Containerization | Docker + Docker Compose | Simplified multi-service setup         |
