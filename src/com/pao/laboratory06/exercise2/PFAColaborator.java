package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM_ANUAL = 4050.0 * 12; // 48600 lei/an

    public PFAColaborator() {
        super(TipColaborator.PFA);
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;

        double impozitVenit = 0.10 * venitNet;

        // CASS (10%)
        double cass;
        if (venitNet < 6 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_ANUAL);
        } else if (venitNet <= 72 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_ANUAL);
        }

        // CAS (25%)
        double cas;
        if (venitNet < 12 * SALARIU_MINIM_ANUAL) {
            cas = 0;
        } else if (venitNet <= 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_ANUAL);
        }

        return venitNet - impozitVenit - cass - cas;
    }
}
