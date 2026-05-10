package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

public class TranzactieEx3 extends Tranzactie {
    private String contSursa;

    public TranzactieEx3(int id, double suma, String data, TipTranzactie tip, String contSursa) {
        super(id, suma, data, tip);
        this.contSursa = contSursa;
    }

    public String getContSursa() { return contSursa; }
}
