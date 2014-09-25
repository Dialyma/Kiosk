package com.dlohaiti.dlokiosk.client;

import java.math.BigDecimal;

public class PriceJson {
    private BigDecimal amount;
    private String currencyCode;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
