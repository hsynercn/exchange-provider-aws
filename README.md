# exchange-provider-aws

Fetches EUR based historic exchange rates from ECB and generates exchange rate graph with daily changes. Spring Boot app with AWS Lambda, provides data for exchange-ui. 

[Link to app](https://www.exchange-ui.com/)(coldstart may cause loading delays)

[Link to frontend repo](https://github.com/hsynercn/exchange-ui)


Functions

- **CurrencyDataFetcher:** Raw exchange data fetcher. Stores processed graph on DynamoDB. Time triggered inner function.
- **GetCurrency:** Provides exchange rate data to frontend, only API gateway connected lambda function.

ECB updates the data set daily. It does not contain any stock, metal or coin.
