package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public final class Transaction {
    private final int id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String country;
    private final String channel;

    public Transaction(int id, String amount, String date, String country, String channel) {
        this.id = id;
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        this.date = LocalDate.parse(date);
        this.country = country;
        this.channel = channel;
    }

    public int getId()            { return id; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate()    { return date; }
    public String getCountry()    { return country; }
    public String getChannel()    { return channel; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s %s %s",
                id, amount.toPlainString(), date, country, channel);
    }
}
