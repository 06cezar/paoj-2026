-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS order_products;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS delivery_addresses;
DROP TABLE IF EXISTS drivers;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS restaurants;

CREATE TABLE restaurants (
    id           VARCHAR(50)  PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    street       VARCHAR(200) NOT NULL,
    city         VARCHAR(100) NOT NULL,
    postal_code  VARCHAR(20)  NOT NULL,
    category     VARCHAR(50)  NOT NULL
);

CREATE TABLE clients (
    id    VARCHAR(50)  PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE delivery_addresses (
    id          VARCHAR(50)  PRIMARY KEY,
    client_id   VARCHAR(50)  NOT NULL,
    street      VARCHAR(200) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20)  NOT NULL,
    details     VARCHAR(200),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE TABLE drivers (
    id        VARCHAR(50)  PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    email     VARCHAR(100) NOT NULL,
    phone     VARCHAR(20),
    rating    DOUBLE       NOT NULL DEFAULT 5.0,
    available BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE products (
    id            VARCHAR(50)  PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    price         DOUBLE       NOT NULL,
    description   VARCHAR(500),
    category      VARCHAR(50)  NOT NULL,
    restaurant_id VARCHAR(50)  NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id                  VARCHAR(50) PRIMARY KEY,
    client_id           VARCHAR(50) NOT NULL,
    restaurant_id       VARCHAR(50) NOT NULL,
    driver_id           VARCHAR(50),
    delivery_address_id VARCHAR(50) NOT NULL,
    status              VARCHAR(30) NOT NULL,
    order_date          DATETIME    NOT NULL,
    total_price         DOUBLE      NOT NULL,
    FOREIGN KEY (client_id)           REFERENCES clients(id),
    FOREIGN KEY (restaurant_id)       REFERENCES restaurants(id),
    FOREIGN KEY (driver_id)           REFERENCES drivers(id) ON DELETE SET NULL,
    FOREIGN KEY (delivery_address_id) REFERENCES delivery_addresses(id)
);

CREATE TABLE order_products (
    order_id   VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
