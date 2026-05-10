package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) {
        List<TranzactieEx3> tranzactii = List.of(
                new TranzactieEx3(1,  1500.00, "2024-01-10", TipTranzactie.CREDIT, "RO11RNCB001"),
                new TranzactieEx3(2,   750.50, "2024-01-15", TipTranzactie.DEBIT,  "RO22BRDE002"),
                new TranzactieEx3(3,   200.00, "2024-01-22", TipTranzactie.DEBIT,  "RO11RNCB001"),
                new TranzactieEx3(4,  3000.00, "2024-02-05", TipTranzactie.CREDIT, "RO33INGB003"),
                new TranzactieEx3(5,   450.00, "2024-02-14", TipTranzactie.DEBIT,  "RO22BRDE002"),
                new TranzactieEx3(6,  1200.00, "2024-02-20", TipTranzactie.CREDIT, "RO11RNCB001"),
                new TranzactieEx3(7,    80.00, "2024-02-28", TipTranzactie.DEBIT,  "RO33INGB003"),
                new TranzactieEx3(8,  2500.00, "2024-03-03", TipTranzactie.CREDIT, "RO44BTRL004"),
                new TranzactieEx3(9,   300.00, "2024-03-17", TipTranzactie.DEBIT,  "RO22BRDE002"),
                new TranzactieEx3(10,  950.00, "2024-03-25", TipTranzactie.CREDIT, "RO44BTRL004")
        );

        // 1. filter(tip == CREDIT)
        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        // 2. mapToDouble(suma).sum()
        System.out.println("\n=== 2. Total procesat ===");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", total);

        // 3. Collectors.groupingBy(luna, summingDouble(suma))
        System.out.println("\n=== 3. Total per luna ===");
        tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ))
                .forEach((luna, suma) -> System.out.printf("%s: %.2f RON%n", luna, suma));

        // 4. sorted(comparingDouble.reversed()).limit(3)
        System.out.println("\n=== 4. Top 3 tranzactii ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        // 5. map(contSursa).distinct().collect(toList())
        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(TranzactieEx3::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        // 6. mapToDouble(suma).average()
        System.out.println("\n=== 6. Suma medie ===");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", medie);

        // 7. Collectors.groupingBy(luna) cu format extras
        System.out.println("\n=== 7. Extras de cont lunar ===");
        tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ))
                .forEach((luna, lista) -> {
                    double totalLuna = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
                    System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                            luna, lista.size(), totalLuna);
                });
    }
}
