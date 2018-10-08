package CryptocurrencyPriceSentiments.mappers;

import CryptocurrencyPriceSentiments.models.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO `crypto-compare`.`users` (`api_key`, `ip_address`, `user_agent`) " +
            "VALUES (#{apiKey}, #{ipAddress}, #{userAgent});")
    public int addUser(User user);

    @Select("SELECT * FROM `crypto-compare`.`users` WHERE `api_key` = #{apiKey} LIMIT 1;")
    @Results(id = "User", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "api_key", property = "apiKey"),
            @Result(column = "ip_address", property = "ipAddress"),
            @Result(column = "user_agent", property = "userAgent")
    })
    public User validateUser(String apiKey);
}
