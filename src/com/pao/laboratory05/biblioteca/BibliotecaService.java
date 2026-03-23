package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

// BibliotecaService.java
public class BibliotecaService {
    private Carte[] carti;

    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] newCarti = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, newCarti, 0, carti.length);
        newCarti[carti.length] = carte;
        carti = newCarti;
        System.out.println("Adaugat: " + carte);
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy);
        for (Carte c : copy) System.out.println(c);
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);
        for (Carte c : copy) System.out.println(c);
    }
}