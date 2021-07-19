package domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName="ExchangeData")
public class ExchangeData implements Serializable {

    private String id;
    private String instanceDate;
    private String mainCode;
    private String date;
    private String exchangeDataJson;

    @DynamoDBHashKey(attributeName="id")
    public String getId() { return id; }
    public void setId(String id) {this.id = id; }

    @DynamoDBAttribute(attributeName="InstanceDate")
    public String getInstanceDate() {return instanceDate; }
    public void setInstanceDate(String instanceDate) { this.instanceDate = instanceDate; }

    @DynamoDBAttribute(attributeName="MainCode")
    public String getMainCode() {return mainCode; }
    public void setMainCode(String mainCode) { this.mainCode = mainCode; }

    @DynamoDBAttribute(attributeName="Date")
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @DynamoDBAttribute(attributeName="ExchangeRates")
    public String getExchangeDataJson() { return exchangeDataJson; }
    public void setExchangeDataJson(String exchangeDataJson) { this.exchangeDataJson = exchangeDataJson; }

}
