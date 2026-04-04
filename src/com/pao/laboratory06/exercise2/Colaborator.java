package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public abstract class Colaborator implements IOperatiiCitireScriere, Comparable<Colaborator> {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;
    protected TipColaborator tip;

    public Colaborator(TipColaborator tip) {
        this.tip = tip;
    }

    public abstract double calculeazaVenitNetAnual();

    public TipColaborator getTip() { return tip; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }

    @Override
    public int compareTo(Colaborator other) {
        return Double.compare(other.calculeazaVenitNetAnual(), this.calculeazaVenitNetAnual());
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei%n",
                tip, nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return tip.name();
    }
}
