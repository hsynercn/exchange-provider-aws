package domain;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class ExchangeDataDBHandler {
    public static void putItemOne(ExchangeData exchangeData) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);
        exchangeData.setId("1");
        mapper.save(exchangeData);
    }
}
