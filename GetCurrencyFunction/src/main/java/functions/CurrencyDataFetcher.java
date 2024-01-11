package functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import datasourceutil.EuropeanCentralBankCurrencySource;
import domain.ExchangeData;
import domain.ExchangeDataDBHandler;
import model.ExchangeRate;
import model.ExchangeRateList;
import sessiondata.ExchangeDataRepository;
import util.GraphCalculator;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataFetcher implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        ExchangeRateList result = null;
        try {
            result = EuropeanCentralBankCurrencySource.extractData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mainCurrencyCode = "USD";
        ArrayList<ExchangeRate> traversalList = GraphCalculator.traversal(result, mainCurrencyCode);

        ExchangeDataRepository exchangeDataRepository = new ExchangeDataRepository();
        exchangeDataRepository.setDate(result.getDate());
        exchangeDataRepository.setMainCode(mainCurrencyCode);
        exchangeDataRepository.setExchangeRates(traversalList);

        ExchangeData exchangeData = new ExchangeData();
        exchangeData.setExchangeDataJson(exchangeDataRepository.toString());
        exchangeData.setDate(exchangeDataRepository.getDate().toString());
        exchangeData.setMainCode(exchangeDataRepository.getMainCode());
        exchangeData.setInstanceDate(new Date().toString());
        ExchangeDataDBHandler.putItemOne(exchangeData);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        return response
                .withStatusCode(200)
                .withBody(exchangeDataRepository.toString());
    }
}
