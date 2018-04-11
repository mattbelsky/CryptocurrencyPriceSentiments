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

    // Statements to add prices by specific criteria.
    String ADD_PRICE_BY_DATE = "INSERT INTO `crypto-compare`.`data_by_date` " +
            "(`day`, `month`, `year`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{day}, #{month}, #{year}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});";
    String ADD_PRICE_BY_HOUR_DATE = "INSERT INTO `crypto-compare`.`data_by_hour` " +
            "(`hour`, `day`, `month`, `year`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{hour}, #{day}, #{month}, #{year}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});";
    String ADD_PRICE_BY_MIN_HOUR_DATE = "INSERT INTO `crypto-compare`.`data_by_minute` " +
            "(`minute`, `hour`, `day`, `month`, `year`, `close`, `high`, `low`, `volumeFrom`, `volumeTo`) " +
            "VALUES (#{minute}, #{hour}, #{day}, #{month}, #{year}, #{close}, #{high}, #{low}, #{volumefrom}, #{volumeto});";

    // Statements to select prices by specific criteria.
    String SELECT_PRICE_BY_DATE = "SELECT * FROM `crypto-compare`.`data_by_date` WHERE date = #{date};";
    String SELECT_PRICE_BY_HOUR_AND_DATE = "SELECT * FROM `crypto-compare`.`data_by_hour` WHERE date = #{date} AND hour = #{hour};";
    String SELECT_PRICE_BY_MIN_AND_HOUR_AND_DATE = "SELECT * FROM `crypto-compare`.`data_by_minute` WHERE date = #{date} AND hour = #{hour} AND min = #{min};";

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

    @Insert(ADD_PRICE_BY_MIN_HOUR_DATE)
    public int addPriceByMin(Data dataByMinute);

    // Get prices by specific criteria.
    @Select(SELECT_PRICE_BY_DATE)
    public Data[] getPriceByDate();

    @Select(SELECT_PRICE_BY_HOUR_AND_DATE)
    public Data[] getPriceByHourAndDate();

    @Select(SELECT_PRICE_BY_MIN_AND_HOUR_AND_DATE)
    public Data[] getPriceByMinAndDate();
}
