package CryptocurrencyPriceSentiments;

import CryptocurrencyPriceSentiments.models.sentiment_analysis.CurrencySentiment;
import CryptocurrencyPriceSentiments.models.Data;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.PriceChangeDbEntity;
import CryptocurrencyPriceSentiments.models.sentiment_analysis.PriceChangeSummary;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.ArrayList;

@Mapper
public interface CryptoMapper {

    String SELECT_NEWS_BY_CATEGORY = "SELECT * FROM `komodoDB`.`news` WHERE categories LIKE '%${category}%';";

    // Adds data to the date table.
    @Insert("INSERT IGNORE INTO `crypto-compare`.`data_by_date` " +
            "(`time`, `fromCurrency`, `toCurrency`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{fromCurrency}, #{toCurrency}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});")
    public int addPriceByDate(Data dataByDate);

    // Adds data to the hour table.
    @Insert("INSERT IGNORE INTO `crypto-compare`.`data_by_hour` " +
            "(`time`, `fromCurrency`, `toCurrency`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{fromCurrency}, #{toCurrency}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});")
    public int addPriceByHour(Data dataByHour);

    // Adds data to the minute table.
    @Insert("INSERT IGNORE INTO `crypto-compare`.`data_by_minute` " +
            "(`time`, `fromCurrency`, `toCurrency`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{fromCurrency}, #{toCurrency}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});")
    public int addPriceByMinute(Data dataByMinute);


    // Gets all data from the day table.
    @Select("SELECT * FROM `crypto-compare`.`data_by_date`;")
    public Data[] getDataByDay();

    // Gets all data from the day table.
    @Select("SELECT * FROM `crypto-compare`.`data_by_hour`;")
    public Data[] getDataByHour();

    // Gets all data from the day table.
    @Select("SELECT * FROM `crypto-compare`.`data_by_minute`;")
    public Data[] getDataByMinute();


    // Gets data from the day data for the specified timestamp.
    @Select("SELECT * FROM `crypto-compare`.`data_by_date` WHERE `time` = #{time};")
    public Data[] getDailyDataByTime(int time);

    // Gets data from the day data for the specified timestamp.
    @Select("SELECT * FROM `crypto-compare`.`data_by_hour` WHERE `time` = #{time};")
    public Data[] getHourlyDataByTime(int time);

    // Gets data from the day data for the specified timestamp.
    @Select("SELECT * FROM `crypto-compare`.`data_by_minute` WHERE `time` = #{time};")
    public Data[] getMinutelyDataByTime(int time);


    // Gets all timestamps by the specified time period and currency pair.
    @Select("SELECT `time` FROM `crypto-compare`.`data_by_${arg0}` " +
            "WHERE `fromCurrency` = #{arg1} AND `toCurrency` = #{arg2} ORDER BY time ASC;")
    public Integer[] getTimestampsByPeriod(String period, String fromCurrency, String toCurrency);

    // Counts the number of records by time period and currency pair.
    @Select("SELECT COUNT(id) FROM `crypto-compare`.`data_by_${arg0}` " +
            "WHERE `fromCurrency` = #{arg1} AND `toCurrency` = #{arg2};")
    public int countRecordsByPeriod(String period, String fromCurrency, String toCurrency);

    // Gets the last timestamp by time period and currency pair.
    @Select("SELECT `time` FROM `crypto-compare`.`data_by_${arg0}` " +
            "WHERE `fromCurrency` = #{arg1} AND `toCurrency` = #{arg2} ORDER BY `time` ASC LIMIT 1;")
    public int getLastTimestamp(String period, String fromCurrency, String toCurrency);

    // Adds news data to the database.
    @Insert("INSERT IGNORE INTO `crypto-compare`.`news` " +
            "(`articleId`, `publishedOn`, `title`, `url`, `body`, `tags`, `categories`) " +
            "VALUES (#{articleId}, #{publishedOn}, #{title}, #{url}, #{body}, #{tags}, #{categories});")
    public int addNews(CryptocurrencyPriceSentiments.models.news.Data newsData);

    // Gets all news data from the database.
    @Select("SELECT * FROM `crypto-compare`.`news` LIMIT 50;")
    public ArrayList<CryptocurrencyPriceSentiments.models.news.Data> getNews();

    // Gets news by category.
    @Select("SELECT * FROM `crypto-compare`.`news` WHERE `categories` LIKE CONCAT('%', #{categories}, '%');")
    public ArrayList<CryptocurrencyPriceSentiments.models.news.Data> getNewsByCategory(String categories);

    // Gets the number of currencies being traded.
    @Select("SELECT COUNT(id) FROM `crypto-compare`.`currencies`;")
    public int getNumCurrencies();

    // Gets a list of the currencies being traded.
    @Select("SELECT `symbol` FROM `crypto-compare`.`currencies`;")
    public ArrayList<String> getCurrencies();

    // Gets trading symbols for each currency pair.
    @Select("SELECT CONCAT(`from_symbol`, '/', `to_symbol`) FROM `crypto-compare`.`currency_pairs`;")
    public String[] getCurrencyPairs();

    // Adds sentiment data.
    @Insert("INSERT IGNORE INTO `crypto-compare`.`currencies_sentiments` (`currency_symbol`, `published_on`, `sentiment`, `score`) " +
            "VALUES (#{currencySymbol}, #{publishedOn}, #{sentiment}, #{score});")
    public int addSentiments(CurrencySentiment sentiment);

    // Gets all sentiments associated with all news stories.
    @Select("SELECT * FROM `crypto-compare`.`currencies_sentiments` LIMIT 50;")
    @Results(id = "CurrencySentiment", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "currency_symbol", property = "currencySymbol"),
            @Result(column = "published_on", property = "publishedOn"),
            @Result(column = "sentiment", property = "sentiment"),
            @Result(column = "score", property = "score")})
    public ArrayList<CurrencySentiment> getSentiments();

    // Gets a list of tone names that Watson returns.
    @Select("SELECT `tone` FROM `crypto-compare`.`watson_tones`;")
    public String[] getToneNames();

    // Calls a stored procedure that generates a view joining several tables and selects price change summary data from
    // this view based on the supplied currency and sentiment direction (positive or negative).
    @Select("CALL `crypto-compare`.getClosingPriceByCurrencyAndToneDirection(" +
            "#{inCurrencyName, mode = IN, jdbcType = VARCHAR}, " +
            "#{inToneDirection, mode = IN, jdbcType = VARCHAR}, " +
            "#{outCurrencyName, mode = OUT, jdbcType = VARCHAR}, " +
            "#{outToneDirection, mode = OUT, jdbcType = VARCHAR}, " +
            "#{outProportionSuccess, mode = OUT, jdbcType = DECIMAL});")
    @Results(value = {
            @Result(column = "inCurrencyName", property = "inCurrencyName"),
            @Result(column = "inToneDirection", property = "inToneDirection"),
            @Result(column = "outCurrencyName", property = "outCurrencyName"),
            @Result(column = "outToneDirection", property = "outToneDirection"),
            @Result(column = "outProportionSuccess", property = "outProportionSuccess")
            })
    @ResultType(PriceChangeDbEntity.class)
    @Options(statementType = StatementType.CALLABLE)
    public PriceChangeDbEntity getPriceChangeByCurrencyAndToneDirection(PriceChangeDbEntity params);
}
