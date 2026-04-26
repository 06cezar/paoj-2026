package com.pao.project.fooddelivery.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String mesaj) {
        super(mesaj);
    }
}
