package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        List<Transaction> data = List.of(
                new Transaction(1,  "1200.00", "2026-05-01", "RO", "WEB"),
                new Transaction(2,  "300.00",  "2026-05-03", "RU", "ATM"),
                new Transaction(3,  "6000.00", "2026-05-10", "NG", "CRYPTO"),
                new Transaction(4,  "90.00",   "2026-06-01", "RO", "APP"),
                new Transaction(5,  "500.00",  "2026-06-15", "RO", "WEB"),
                new Transaction(6,  "1200.00", "2026-06-20", "US", "WEB"),   // tie cu tx1
                new Transaction(7,  "450.00",  "2026-07-01", "DE", "ATM"),
                new Transaction(8,  "8000.00", "2026-07-04", "IR", "CRYPTO"),
                new Transaction(9,  "75.00",   "2026-07-09", "RO", "POS"),
                new Transaction(10, "2200.00", "2026-07-22", "RO", "APP")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(5));

        // Interogare 1: sumar general
        System.out.println("=== Interogare 1: Sumar general ===");
        System.out.printf("  Total tranzactii : %d%n", snap.getTransactionCount());
        System.out.printf("  Suma totala      : %s%n", snap.getTotalAmount().toPlainString());

        // Interogare 2: top 5 tranzactii dupa suma (tie-break: id ASC)
        System.out.println("\n=== Interogare 2: Top 5 tranzactii dupa suma ===");
        snap.getTopTransactions().forEach(tx -> System.out.printf("  %s%n", tx));

        // Interogare 3: count tranzactii per tara, descendent
        System.out.println("\n=== Interogare 3: Tranzactii per tara (desc count, asc tara) ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("  %-5s : %d%n", e.getKey(), e.getValue()));

        // Interogare 4: canale - count + suma totala, ordonate dupa count desc
        System.out.println("\n=== Interogare 4: Canale - count si suma (dupa count desc) ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> {
                    BigDecimal sum = snap.getTotalByChannel().get(e.getKey());
                    System.out.printf("  %-8s count=%-3d total=%s%n",
                            e.getKey(), e.getValue(), sum.toPlainString());
                });

        // Interogare 5: verifica imutabilitatea snapshot-ului
        System.out.println("\n=== Interogare 5: Imutabilitate snapshot ===");
        try {
            snap.getTopTransactions().add(null);
            System.out.println("  FAIL: lista e mutabila!");
        } catch (UnsupportedOperationException ex) {
            System.out.println("  OK: topTransactions este read-only.");
        }
        try {
            snap.getCountByCountry().put("XX", 999L);
            System.out.println("  FAIL: map e mutabil!");
        } catch (UnsupportedOperationException ex) {
            System.out.println("  OK: countByCountry este read-only.");
        }
    }
}
