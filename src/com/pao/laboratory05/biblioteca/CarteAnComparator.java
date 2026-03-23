package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAnComparator implements Comparator<Carte> {

    @Override
    public int compare(Carte o1, Carte o2) {
        return Integer.compare(o1.getAn(), o2.getAn());
    }
}
