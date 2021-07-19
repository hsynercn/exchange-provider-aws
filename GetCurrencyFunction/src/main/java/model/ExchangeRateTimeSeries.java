package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class ExchangeRateTimeSeries {
    private final String numeratorEntityCode = "EUR";
    private HashMap<Date, HashMap<String, ExchangeRate>> timeCurrencyMap = new HashMap<>();

    public Set<Date> getDates() {
        return timeCurrencyMap.keySet();
    }

    public void putExchangeRate(Date date, ExchangeRate ecbExchangeRate) {
        if(timeCurrencyMap.containsKey(date) == false) {
            timeCurrencyMap.put(date, new HashMap<>());
        }
        timeCurrencyMap.get(date).put(ecbExchangeRate.getDenominatorEntityCode(), ecbExchangeRate);
    }

    public HashMap<String, ExchangeRate> getDateCurrencyMap(Date date) {
        return timeCurrencyMap.get(date);
    }

}
