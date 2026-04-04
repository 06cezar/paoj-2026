package com.pao.laboratory06.exercise2;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Colaborator[] colaboratori = new Colaborator[n];

        for (int i = 0; i < n; i++) {
            String tip = scanner.next();
            Colaborator c = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            c.citeste(scanner);
            colaboratori[i] = c;
        }


        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }

        // 1. Sortare descrescatoare dupa venit net anual
        Arrays.sort(colaboratori);

        // 2. Colaboratorul cu venit net maxim (primul dupa sortare)
        System.out.println();
        System.out.print("Colaborator cu venit net maxim: ");
        colaboratori[0].afiseaza();

        // 3. Doar persoane juridice (SRL)
        System.out.println();
        System.out.println("Colaboratori persoane juridice:");
        for (Colaborator c : colaboratori) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }

        // 4. Suma si numar per tip
        System.out.println();
        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            double suma = 0;
            int numar = 0;
            for (Colaborator c : colaboratori) {
                if (c.getTip() == tip) {
                    suma += c.calculeazaVenitNetAnual();
                    numar++;
                }
            }
            if (numar > 0) {
                System.out.printf("%s: suma = %.2f lei, număr = %d%n", tip, suma, numar);
            }
            else {
                System.out.printf("%s: suma = nu lei, număr = null%n", tip);
            }
        }
    }
}
