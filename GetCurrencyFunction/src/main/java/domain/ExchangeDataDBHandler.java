package domain;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;


import java.util.List;

public class ExchangeDataDBHandler {
    public static void putItemOne(ExchangeData exchangeData) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        exchangeData.setId("1");
        mapper.save(exchangeData);
    }

    public static ExchangeData getItemOne() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        ExchangeData exchangeData = new ExchangeData();

        exchangeData.setId("1");
        DynamoDBQueryExpression<ExchangeData> queryExpression = new DynamoDBQueryExpression<ExchangeData>()
                .withHashKeyValues(exchangeData);

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        List<ExchangeData> resultList = mapper.query(ExchangeData.class, queryExpression);

        return resultList.get(0);
    }
}
