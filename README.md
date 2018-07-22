# CryptocurrencyPriceSentiments
Determines whether the tone of news stories affects cryptocurrency prices.

Historical price data and news stories are gathered from CryptoCompare, and sentiment analysis is performed by IBM Watson Tone Analyzer.

UNDER DEVELOPMENT: Will determine whether price change after news stories of varying moods is statistically correlated.

### Prerequisites
You will need a user name and password from [IBM Cloud Services](https://www.ibm.com/watson/services/tone-analyzer/) for Watson Tone Analyzer. These should be added to the application.properties file:

```
watson.username=USERNAME
watson.password=PASSWORD
```

### Tech Stack
* Java 8
* Maven
* Spring Boot
* Mybatis
