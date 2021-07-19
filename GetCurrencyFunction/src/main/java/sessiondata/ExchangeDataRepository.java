package sessiondata;

import model.ExchangeRate;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class ExchangeDataRepository {
    private String mainCode;
    private Date date;
    private ArrayList<ExchangeRate> exchangeRates;

    public String getMainCode() {
        return mainCode;
    }

    public void setMainCode(String mainCode) {
        this.mainCode = mainCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ArrayList<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    @Override
    public String toString() {
        return "{"
                + "\"main\":\"" + mainCode + "\""
                + ",\"date\":\"" + date + "\""
                + ",\"rates\":" + exchangeRates.stream().map(
                exchangeRate -> exchangeRate.toString()).collect(Collectors.joining(", ", "[", "]"))
                + "}";
    }
}
