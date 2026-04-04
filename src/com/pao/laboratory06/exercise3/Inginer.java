package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
        this.autentificat = false;
    }

    @Override
    public int compareTo(Inginer other) {
        return this.nume.compareTo(other.nume);
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("User sau parola nu pot fi null/goale.");
        }
        this.autentificat = true;
        System.out.println("[Inginer] " + nume + " autentificat cu succes.");
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (sold >= suma) {
            sold -= suma;
            System.out.printf("[Inginer] %s a platit %.2f lei. Sold ramas: %.2f%n", nume, suma, sold);
            return true;
        }
        System.out.println("[Inginer] " + nume + " — sold insuficient.");
        return false;
    }

    @Override
    public String toString() {
        return String.format("Inginer{%s %s, salariu=%.2f, sold=%.2f}", nume, prenume, salariu, sold);
    }
}
