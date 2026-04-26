package com.pao.project.fooddelivery.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String mesaj) {
        super(mesaj);
    }
}
