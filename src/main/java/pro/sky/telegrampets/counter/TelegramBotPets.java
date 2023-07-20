package pro.sky.telegrampets.counter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.config.TelegramBotConfiguration;

@Component
public class TelegramBotPets extends TelegramLongPollingBot {
    private final TelegramBotConfiguration telegramBotConfiguration;

    public TelegramBotPets(TelegramBotConfiguration telegramBotConfiguration) {
        this.telegramBotConfiguration = telegramBotConfiguration;
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfiguration.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();
        String userFirstName = update.getMessage().getFrom().getFirstName();

        SendMessage message = new SendMessage(String.valueOf(chatId), "Hello " + userFirstName);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}