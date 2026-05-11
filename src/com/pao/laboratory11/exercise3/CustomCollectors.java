package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CustomCollectors {

    private CustomCollectors() {}

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) { // param topN numarul de tranzactii cu suma cea mai mare incluse in snapshot
                                                                             // (tie-breaker: id ascendent)

        class Agg {
            BigDecimal total = BigDecimal.ZERO;
            long count = 0;
            final Map<String, Long> byCountry = new HashMap<>();
            final Map<String, Long> byChannel = new HashMap<>();
            final Map<String, BigDecimal> sumByChannel = new HashMap<>();
            final List<Transaction> all = new ArrayList<>();

            void add(Transaction tx) {
                total = total.add(tx.getAmount());
                count++;
                byCountry.merge(tx.getCountry(), 1L, Long::sum);
                byChannel.merge(tx.getChannel(), 1L, Long::sum);
                sumByChannel.merge(tx.getChannel(), tx.getAmount(), BigDecimal::add);
                all.add(tx);
            }

            Agg combine(Agg other) {
                other.all.forEach(this::add);
                return this;
            }

            Snapshot finish() {
                List<Transaction> top = all.stream()
                        .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                                .thenComparingInt(Transaction::getId))
                        .limit(topN)
                        .collect(Collectors.toList());
                return new Snapshot(total, count, byCountry, byChannel, sumByChannel, top);
            }
        }

        return Collector.of(
                Agg::new,
                Agg::add,
                Agg::combine,
                Agg::finish,
                Collector.Characteristics.UNORDERED
        );
    }
}
