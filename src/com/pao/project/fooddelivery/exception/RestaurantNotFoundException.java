package com.pao.project.fooddelivery.exception;

public class RestaurantNotFoundException extends Exception {
    public RestaurantNotFoundException(String mesaj) {
        super(mesaj);
    }
}
