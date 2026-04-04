package com.pao.laboratory06.exercise3;

import java.util.Comparator;

// Sorteaza inginerii descrescator dupa salariu
public class ComparatorInginerSalariu implements Comparator<Inginer> {
    @Override
    public int compare(Inginer a, Inginer b) {
        return Double.compare(b.getSalariu(), a.getSalariu());
    }
}
