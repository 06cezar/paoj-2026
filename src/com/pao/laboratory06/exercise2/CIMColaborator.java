package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        super(TipColaborator.CIM);
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        if (in.hasNext("[DA|NU]") || in.hasNext("DA") || in.hasNext("NU")) {
            String bonusStr = in.next();
            this.bonus = bonusStr.equals("DA");
        } else {
            this.bonus = false;
        }
    }

    @Override
    public boolean areBonus() { return bonus; }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (bonus) net *= 1.10;
        return net;
    }
}
