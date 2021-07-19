package model;

import java.math.BigDecimal;

public class ExchangeRate {
    private String numeratorEntityCode;
    private String denominatorEntityCode;
    private BigDecimal rate;
    private BigDecimal dailyChange;
    private BigDecimal dailyChangeRatio;

    public ExchangeRate(String numeratorEntityCode, String denominatorEntityCode, BigDecimal rate) {
        this.numeratorEntityCode = numeratorEntityCode;
        this.denominatorEntityCode = denominatorEntityCode;
        this.rate = rate;
    }

    public ExchangeRate(String numeratorEntityCode, String denominatorEntityCode, BigDecimal rate, BigDecimal dailyChange, BigDecimal dailyChangeRatio) {
        this.numeratorEntityCode = numeratorEntityCode;
        this.denominatorEntityCode = denominatorEntityCode;
        this.rate = rate;
        this.dailyChange = dailyChange;
        this.dailyChangeRatio = dailyChangeRatio;
    }

    public String getNumeratorEntityCode() {
        return numeratorEntityCode;
    }

    public void setNumeratorEntityCode(String numeratorEntityCode) {
        this.numeratorEntityCode = numeratorEntityCode;
    }

    public String getDenominatorEntityCode() {
        return denominatorEntityCode;
    }

    public void setDenominatorEntityCode(String denominatorEntityCode) {
        this.denominatorEntityCode = denominatorEntityCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(BigDecimal dailyChange) {
        this.dailyChange = dailyChange;
    }

    public BigDecimal getDailyChangeRatio() {
        return dailyChangeRatio;
    }

    public void setDailyChangeRatio(BigDecimal dailyChangeRatio) {
        this.dailyChangeRatio = dailyChangeRatio;
    }

    @Override
    public String toString() {
        return "{"
                + "\"num\":\"" + numeratorEntityCode + "\""
                + ",\"den\":\"" + denominatorEntityCode + "\""
                + ",\"rt\":\"" + rate + "\""
                + ",\"dChg\":\"" + dailyChange + "\""
                + ",\"dChgR\":\"" + dailyChangeRatio + "\""
                + "}";
    }
}
