package crypto_compare_exercise;

import crypto_compare_exercise.models.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CryptoMapper {

    // Statements to add & select all prices.
    String ADD_PRICES = "INSERT INTO `crypto-compare`.`data` (`time`, `close`, `high`, `low`, `open`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{close}, #{high}, #{low}, #{open}, #{volumefrom}, #{volumeto});";
    String GET_PRICES = "SELECT * FROM `crypto-compare`.`data`";

    // Statements to add data to each of the databases.
    String ADD_PRICE_BY_DATE = "INSERT INTO `crypto-compare`.`data_by_date` " +
            "(`time`, `day`, `month`, `year`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{day}, #{month}, #{year}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});";
    String ADD_PRICE_BY_HOUR_DATE = "INSERT INTO `crypto-compare`.`data_by_hour` " +
            "(`time`, `hour`, `day`, `month`, `year`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{hour}, #{day}, #{month}, #{year}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});";
    String ADD_PRICE_BY_MINUTE_HOUR_DATE = "INSERT INTO `crypto-compare`.`data_by_minute` " +
            "(`time`, `minute`, `hour`, `day`, `month`, `year`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{time}, #{minute}, #{hour}, #{day}, #{month}, #{year}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});";

    // Statements to select data from each of the databases.
    String SELECT_PRICE_BY_DATE = "SELECT * FROM `crypto-compare`.`data_by_date`;";

    String SELECT_TIMESTAMP_BY_DATE = "SELECT time FROM `crypto-compare`.`data_by_date`;";
    String SELECT_TIMESTAMP_BY_HOUR = "SELECT time FROM `crypto-compare`.`data_by_hour`;";
    String SELECT_TIMESTAMP_BY_MINUTE = "SELECT time FROM `crypto-compare`.`data_by_minute`;";

    String SELECT_PRICE_BY_HOUR_AND_DATE = "SELECT * FROM `crypto-compare`.`data_by_hour` WHERE date = #{date} AND hour = #{hour};";
    String SELECT_PRICE_BY_MIN_AND_HOUR_AND_DATE = "SELECT * FROM `crypto-compare`.`data_by_minute` WHERE date = #{date} AND hour = #{hour} AND min = #{min};";

    // Seeks missing values from each of the databases.

    // Add and get all prices.
    @Insert(ADD_PRICES)
    public int addPrices(Data datum);

    @Select(GET_PRICES)
    public Data[] getPrices();

    // Add prices by specific criteria.
    @Insert(ADD_PRICE_BY_DATE)
    public int addPriceByDate(Data priceByDate);

    @Insert(ADD_PRICE_BY_HOUR_DATE)
    public int addPriceByHour(Data dataByHour);

    @Insert(ADD_PRICE_BY_MINUTE_HOUR_DATE)
    public int addPriceByMinute(Data dataByMinute);

    // Get prices by specific criteria.
    @Select(SELECT_PRICE_BY_DATE)
    public Data[] getPriceByDate();


    @Select(SELECT_TIMESTAMP_BY_DATE)
    public Integer[] getTimestampByDate();

    @Select(SELECT_TIMESTAMP_BY_HOUR)
    public Integer[] getTimestampByHour();

    @Select(SELECT_TIMESTAMP_BY_MINUTE)
    public Integer[] getTimestampByMinute();


    @Select(SELECT_PRICE_BY_HOUR_AND_DATE)
    public Data[] getPriceByHourAndDate();

    @Select(SELECT_PRICE_BY_MIN_AND_HOUR_AND_DATE)
    public Data[] getPriceByMinAndDate();
}
