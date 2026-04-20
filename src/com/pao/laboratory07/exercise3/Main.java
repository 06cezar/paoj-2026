package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] t = sc.nextLine().trim().split(" ");
            switch (t[0]) {
                case "STANDARD" -> comenzi.add(new ComandaStandard(t[1], Double.parseDouble(t[2]), t[3]));
                case "DISCOUNTED" -> comenzi.add(new ComandaRedusa(t[1], Double.parseDouble(t[2]), Integer.parseInt(t[3]), t[4]));
                case "GIFT" -> comenzi.add(new ComandaGratuita(t[1], t[2]));
            }
        }

        // Print all orders
        comenzi.forEach(c -> System.out.println(c.descriere()));

        // Process commands
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "STATS" -> stats(comenzi);
                case "FILTER" -> filter(comenzi, Double.parseDouble(parts[1]));
                case "SORT" -> sort(comenzi);
                case "SPECIAL" -> special(comenzi);
                case "QUIT" -> { return; }
            }
        }
    }

    private static void stats(List<Comanda> comenzi) {
        System.out.println("\n--- STATS ---");
        Map<String, Double> medii = comenzi.stream()
                .collect(Collectors.groupingBy(Comanda::tip, Collectors.averagingDouble(Comanda::pretFinal)));

        for (String tip : List.of("STANDARD", "DISCOUNTED", "GIFT")) {
            if (medii.containsKey(tip)) {
                System.out.printf("%s: medie = %.2f lei%n", tip, medii.get(tip));
            }
        }
    }

    private static void filter(List<Comanda> comenzi, double threshold) {
        System.out.printf("%n--- FILTER (>= %.2f) ---%n", threshold);
        comenzi.stream()
                .filter(c -> c.pretFinal() >= threshold)
                .forEach(c -> {
                    if (c instanceof ComandaRedusa r) {
                        System.out.printf("DISCOUNTED: %s, pret: %.2f lei - client: %s%n",
                                r.getNume(), r.pretFinal(), r.getClient());
                    } else if (c instanceof ComandaStandard s) {
                        System.out.printf("STANDARD: %s, pret: %.2f lei - client: %s%n",
                                s.getNume(), s.pretFinal(), s.getClient());
                    } else if (c instanceof ComandaGratuita g) {
                        System.out.printf("GIFT: %s, gratuit - client: %s%n",
                                g.getNume(), g.getClient());
                    }
                });
    }

    private static void sort(List<Comanda> comenzi) {
        System.out.println("\n--- SORT (by client, then by pret) ---");
        comenzi.stream()
                .sorted(Comparator.comparing(Comanda::getClient).thenComparingDouble(Comanda::pretFinal))
                .forEach(c -> {
                    if (c instanceof ComandaRedusa r) {
                        System.out.printf("DISCOUNTED: %s, pret: %.2f lei - client: %s%n",
                                r.getNume(), r.pretFinal(), r.getClient());
                    } else if (c instanceof ComandaStandard s) {
                        System.out.printf("STANDARD: %s, pret: %.2f lei - client: %s%n",
                                s.getNume(), s.pretFinal(), s.getClient());
                    } else if (c instanceof ComandaGratuita g) {
                        System.out.printf("GIFT: %s, gratuit - client: %s%n",
                                g.getNume(), g.getClient());
                    }
                });
    }

    private static void special(List<Comanda> comenzi) {
        System.out.println("\n--- SPECIAL (discount > 15%) ---");
        comenzi.stream()
                .filter(c -> c instanceof ComandaRedusa r && r.getDiscountProcent() > 15)
                .forEach(c -> {
                    ComandaRedusa r = (ComandaRedusa) c;
                    System.out.printf("DISCOUNTED: %s, pret: %.2f lei (-%d%%) - client: %s%n",
                            r.getNume(), r.pretFinal(), r.getDiscountProcent(), r.getClient());
                });
    }
}
