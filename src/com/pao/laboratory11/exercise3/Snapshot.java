package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;

public final class Snapshot {
    private final BigDecimal totalAmount;
    private final long transactionCount;
    private final Map<String, Long> countByCountry;
    private final Map<String, Long> countByChannel;
    private final Map<String, BigDecimal> totalByChannel;
    private final List<Transaction> topTransactions;

    public Snapshot(BigDecimal totalAmount,
                    long transactionCount,
                    Map<String, Long> countByCountry,
                    Map<String, Long> countByChannel,
                    Map<String, BigDecimal> totalByChannel,
                    List<Transaction> topTransactions) {
        this.totalAmount      = totalAmount;
        this.transactionCount = transactionCount;
        this.countByCountry   = Collections.unmodifiableMap(new LinkedHashMap<>(countByCountry));
        this.countByChannel   = Collections.unmodifiableMap(new LinkedHashMap<>(countByChannel));
        this.totalByChannel   = Collections.unmodifiableMap(new LinkedHashMap<>(totalByChannel));
        this.topTransactions  = List.copyOf(topTransactions);
    }

    public BigDecimal getTotalAmount()                { return totalAmount; }
    public long getTransactionCount()                 { return transactionCount; }
    public Map<String, Long> getCountByCountry()      { return countByCountry; }
    public Map<String, Long> getCountByChannel()      { return countByChannel; }
    public Map<String, BigDecimal> getTotalByChannel(){ return totalByChannel; }
    public List<Transaction> getTopTransactions()     { return topTransactions; }
}
