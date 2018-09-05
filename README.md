# CryptocurrencyPriceSentiments
Determines whether the tone of news stories affects cryptocurrency prices.

Historical price data and news stories are gathered from CryptoCompare, and sentiment analysis is performed by IBM Watson Tone Analyzer.

Allows the user to:

* Get price and news historical data
* Perform sentiment analysis on the news data
* Determine the proportion of the time where a positive or negative news story results in a higher or lower price change, respectively
* Choose which Watson tones are considered positive, negative, or neutral.

### Endpoints

``` 
/gethistory?period={period}&numrecords={numrecords}
```
GET: Backloads historical price data for the period "date" or "day", "hour", or "minute" and to the point specified by "numrecords."

```
/news?categories={categories}
```
POST: Queries CryptoCompare for news data of the specified categories ("categories" is optional) and adds this data to the database.

GET: Retrieves news data from the database. Categories are optional again.

```
/news/sentiments
```
POST: Performs sentiment analysis on news stories within the database via the Watson Tone Analyzer and adds this data to the database.

GET: Retrives sentiment data from the database.

```
/news/sentimentssummary
```
GET: Determines the porportion of the time, for each currency,  where a positive or negative news story resutls in a higher or lower price change, respectively, and returns the summarized data.

```
/watsontones/all
```
GET: Gets a list of all the Watson tones and their associated information such as sentiment direction (positive, negative, or neutral).

```
/watsontones/update?tone={tone}&direction={direction}
```
GET: Updates the specified tone with the specified direction.

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
* MySQL
