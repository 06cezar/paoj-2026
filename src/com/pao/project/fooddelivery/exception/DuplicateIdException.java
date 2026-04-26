package com.pao.project.fooddelivery.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String mesaj) {
        super(mesaj);
    }
}
