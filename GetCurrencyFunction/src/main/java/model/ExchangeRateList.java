package model;

import java.util.ArrayList;
import java.util.Date;

public class ExchangeRateList {
    private ArrayList<ExchangeRate> list;
    private Date date;

    public ExchangeRateList(ArrayList<ExchangeRate> list, Date date) {
        this.list = list;
        this.date = date;
    }

    public ArrayList<ExchangeRate> getList() {
        return list;
    }

    public void setList(ArrayList<ExchangeRate> list) {
        this.list = list;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
