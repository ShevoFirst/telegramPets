package pro.sky.telegrampets.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.config.TelegramBotConfiguration;

@Component
public class TelegramBotPets extends TelegramLongPollingBot {
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final Buttons buttons;


    public TelegramBotPets(TelegramBotConfiguration telegramBotConfiguration, @Autowired Buttons buttons) {
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.buttons = buttons;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = buttons.selectionAnimalButtons(chatId, update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) { //проверка на передачу нажатия кнопки
            String callbackData = update.getCallbackQuery().getData(); // название CallbackData кнопки
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("Кошка")) {
                String textCat = "Вы выбрали приют для кошек";
                SendMessage((int) messageId, chatId, textCat);

            } else if (callbackData.equals("Собака")) {
                String textCat = "Вы выбрали приют для собак";
                SendMessage((int) messageId, chatId, textCat);
            }
        }
    }

    private void SendMessage(int messageId, long chatIdInButton, String textCat) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatIdInButton));
        messageText.setText(textCat);
        messageText.setMessageId(messageId);
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}