package datasourceutil;

import model.ExchangeRate;
import model.ExchangeRateList;
import model.ExchangeRateTimeSeries;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EuropeanCentralBankCurrencySource {
    public static ExchangeRateList extractData() throws ParserConfigurationException, IOException, SAXException, ParseException {
        ArrayList<ExchangeRate> result = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml");
        doc.getDocumentElement().normalize();

        Element element = doc.getDocumentElement();
        NodeList childNodes = element.getChildNodes();
        childNodes = childNodes.item(2).getChildNodes();

        ExchangeRateTimeSeries exchangeRateTimeSeries = new ExchangeRateTimeSeries();
        extractThreeMonthData(childNodes, exchangeRateTimeSeries);

        List<Date> dates = new ArrayList<>();
        dates.addAll(exchangeRateTimeSeries.getDates());
        Collections.sort(dates);

        Date dateToday = dates.get(dates.size() - 1);
        Date dateYesterday = dates.get(dates.size() - 2);

        HashMap<String, ExchangeRate> todayExchangeRateMap = exchangeRateTimeSeries.getDateCurrencyMap(dateToday);
        HashMap<String, ExchangeRate> yesterdayExchangeRateMap = exchangeRateTimeSeries.getDateCurrencyMap(dateYesterday);

        for (String currencyCode : todayExchangeRateMap.keySet()) {
            if (yesterdayExchangeRateMap.containsKey(currencyCode)) {
                ExchangeRate exchangeRateToday = todayExchangeRateMap.get(currencyCode);
                ExchangeRate exchangeRateYesterday = yesterdayExchangeRateMap.get(currencyCode);
                ExchangeRate exchangeRateComplete = EuropeanCentralBankCurrencySource.extractCompleteExchange(exchangeRateToday, exchangeRateYesterday);
                result.add(exchangeRateComplete);
            }
        }
        return new ExchangeRateList(result, dateToday);
    }

    private static ExchangeRate extractCompleteExchange(ExchangeRate exchangeRateToday, ExchangeRate exchangeRateYesterday) {
        BigDecimal dailyChange = exchangeRateToday.getRate().subtract(exchangeRateYesterday.getRate());
        BigDecimal dailyChangeRatio = dailyChange.divide(exchangeRateToday.getRate(), 10, RoundingMode.HALF_UP);
        ExchangeRate exchangeRateComplete = new ExchangeRate(
                exchangeRateToday.getNumeratorEntityCode(),
                exchangeRateToday.getDenominatorEntityCode(),
                exchangeRateToday.getRate(),
                dailyChange,
                dailyChangeRatio);
        return exchangeRateComplete;
    }

    private static void extractThreeMonthData(NodeList childNodes, ExchangeRateTimeSeries exchangeRateTimeSeries) throws ParseException {
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) childNodes.item(i);
                String dateStr = el.getAttribute("time");
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                NodeList dateCurrencyNodes = el.getChildNodes();
                for (int j = 0; j < dateCurrencyNodes.getLength(); j++) {
                    Element currencyElement = (Element) dateCurrencyNodes.item(j);
                    String numeratorEntityCode = "EUR";
                    String denominatorEntityCode = currencyElement.getAttribute("currency");
                    String rateStr = currencyElement.getAttribute("rate");
                    BigDecimal rate = new BigDecimal(rateStr);
                    ExchangeRate exchangeRate = new ExchangeRate(numeratorEntityCode, denominatorEntityCode, rate);
                    exchangeRateTimeSeries.putExchangeRate(date, exchangeRate);
                }
            }
        }
    }
}
