package pro.sky.telegrampets.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * a class that stores the token and the name of the bot inside
 */
@Configuration
@Data
public class TelegramBotConfiguration {
    @Value("${telegram.bot.name}")
    String botName;
    @Value("${telegram.bot.token}")
    String token;
}
