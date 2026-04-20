package com.pao.laboratory07.exercise1.exceptions;

public class CannotCancelFinalOrderException extends Exception {
    public CannotCancelFinalOrderException() {
        super("Cannot cancel an order in a final state.");
    }
}
