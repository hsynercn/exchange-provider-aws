package functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import datasourceutil.EuropeanCentralBankCurrencySource;
import model.ExchangeRate;
import model.ExchangeRateList;
import org.xml.sax.SAXException;
import sessiondata.ExchangeDataRepository;
import util.GraphCalculator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyDataFetcher implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        ExchangeRateList result = null;
        try {
            result = EuropeanCentralBankCurrencySource.extractData();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mainCurrencyCode = "USD";
        ArrayList<ExchangeRate> traversalList = GraphCalculator.traversal(result, mainCurrencyCode);

        ExchangeDataRepository exchangeDataRepository = new ExchangeDataRepository();
        exchangeDataRepository.setDate(result.getDate());
        exchangeDataRepository.setMainCode(mainCurrencyCode);
        exchangeDataRepository.setExchangeRates(traversalList);

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
