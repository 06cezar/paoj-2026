package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected OrderState state;

    public Comanda(String nume, String client) {
        this.nume = nume;
        this.client = client;
        this.state = OrderState.PLACED;
    }

    public String getNume() { return nume; }
    public String getClient() { return client; }
    public abstract double pretFinal();
    public abstract String descriere();
    public abstract String tip();
}
