package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS{
    private double sold;
    private List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("User sau parola nu pot fi null/goale.");
        }
        System.out.println("[PersoanaJuridica] " + nume + " autentificata cu succes.");
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
            System.out.printf("[PersoanaJuridica] %s a platit %.2f lei. Sold ramas: %.2f%n", nume, suma, sold);
            return true;
        }
        System.out.println("[PersoanaJuridica] " + nume + " — sold insuficient.");
        return false;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) {
            System.out.println("[SMS] Mesaj invalid — nu s-a trimis.");
            return false;
        }
        if (telefon == null || telefon.isEmpty()) {
            System.out.println("[SMS] " + nume + " nu are numar de telefon — nu s-a trimis.");
            return false;
        }
        smsTrimise.add(mesaj);
        System.out.println("[SMS] Trimis catre " + telefon + ": " + mesaj);
        return true;
    }

    public List<String> getSmsTrimise() { return smsTrimise; }

    @Override
    public String toString() {
        return String.format("PersoanaJuridica{%s %s, sold=%.2f, smsCount=%d}", nume, prenume, sold, smsTrimise.size());
    }
}
