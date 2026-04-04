package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        Inginer[] ingineri = {
                new Inginer("Popescu", "Ion",   "0721000001", 9000, 5000),
                new Inginer("Andrei",  "Maria", "0721000002", 12000, 8000),
                new Inginer("Zamfir",  "Liviu", null,         7500, 3000),
                new Inginer("Barbu",   "Elena", "0721000004", 11000, 6000),
        };

        Arrays.sort(ingineri);
        System.out.println("=== Ingineri sortati alfabetic dupa nume ===");
        for (Inginer i : ingineri) System.out.println(i);

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\n=== Ingineri sortati descrescator dupa salariu ===");
        for (Inginer i : ingineri) System.out.println(i);


        PlataOnline platitor = ingineri[0];
        platitor.autentificare("user1", "pass1");
        System.out.printf("\n[CAN_DO] Sold prin referinta PlataOnline: %.2f lei%n", platitor.consultareSold());
        platitor.efectuarePlata(1000);
        // platitor.getSalariu();

        PersoanaJuridica firma1 = new PersoanaJuridica("TechSRL", "SRL", "0731999999", 50000);
        PersoanaJuridica firmaFaraTelefon = new PersoanaJuridica("MicroSRL", "SRL", null, 20000);

        PlataOnlineSMS smsPlatitor = firma1;
        smsPlatitor.autentificare("techsrl", "secret");
        smsPlatitor.efectuarePlata(5000);

        System.out.println("\n=== Demo SMS ===");
        smsPlatitor.trimiteSMS("Plata confirmata: 5000 lei");
        smsPlatitor.trimiteSMS("Al doilea SMS trimis cu succes");
        smsPlatitor.trimiteSMS("");
        smsPlatitor.trimiteSMS(null);

        System.out.println("SMS-uri stocate pentru " + firma1.getNume() + ": " + firma1.getSmsTrimise());

        System.out.println("\n=== Demo SMS fara telefon ===");
        firmaFaraTelefon.trimiteSMS("Test");  // telefon null → false

        System.out.println("\n=== Demo UnsupportedOperationException ===");
        PlataOnline platitorGeneric = ingineri[1];
        try {
            PlataOnlineSMS smsGresit = (PlataOnlineSMS) platitorGeneric;
            smsGresit.trimiteSMS("Nu ar trebui sa ajunga aici");
        } catch (ClassCastException e) {
            System.out.println("[EROARE TRATATA] Inginerul nu are capabilitate SMS: " + e.getMessage());
        }

        System.out.println("\n=== Demo autentificare invalida ===");
        try {
            ingineri[0].autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("[EROARE TRATATA] " + e.getMessage());
        }
        try {
            ingineri[0].autentificare("user", "");
        } catch (IllegalArgumentException e) {
            System.out.println("[EROARE TRATATA] " + e.getMessage());
        }

        System.out.println("\n=== Constante financiare ===");
        for (ConstanteFinanciare c : ConstanteFinanciare.values()) {
            System.out.printf("%s = %.2f%n", c.name(), c.getValoare());
        }

        double salariuBrut = 8000;
        double impozit = salariuBrut * ConstanteFinanciare.COTA_IMPOZIT.getValoare();
        System.out.printf("%nImpozit pentru salariu brut %.2f lei: %.2f lei%n", salariuBrut, impozit);
    }
}
