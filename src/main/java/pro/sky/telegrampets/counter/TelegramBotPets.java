package pro.sky.telegrampets.counter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.config.TelegramBotConfiguration;

/**
 * класс реагирущий на реакции бота через telegramm api
 */
@Component
public class TelegramBotPets extends TelegramLongPollingBot {
    //подключение конфигуратора к нашему боту
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final Buttons buttons;


    public TelegramBotPets(TelegramBotConfiguration telegramBotConfiguration, Buttons buttons) {
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.buttons = buttons;
    }

    /**
     * получение имени бота
     */
    @Override
    public String getBotUsername() {
        return telegramBotConfiguration.getBotName();
    }

    /**
     * получение токена бота
     */
    @Override
    public String getBotToken() {
        return telegramBotConfiguration.getToken();
    }

    /**
     * Метод получащий все события внутри бота
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {
        //в случае если пользователь введет команду "/start" выведутся кнопки первого уровня
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = buttons.selectionAnimalButtons(chatId, update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        //в случае если пользователь нажал на одну из кнопок то они заменятся на текст и выведутся кнопки 2 уровня
        } else if (update.hasCallbackQuery()) { //проверка на передачу нажатия кнопки
            String callbackData = update.getCallbackQuery().getData(); // название CallbackData кнопки
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("Кошка")) {
                String messageText = "Вы выбрали приют для кошек";
                changeMessage((int) messageId, chatId, messageText);
                SendMessage catsButtons = buttons.secondLayerButtons(chatId);
                try {
                    execute(catsButtons);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (callbackData.equals("Собака")) {
                String textCat = "Вы выбрали приют для собак";
                changeMessage((int) messageId, chatId, textCat);
                SendMessage dogButtons = buttons.secondLayerButtons(chatId);
                try {
                    execute(dogButtons);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            //в случае если не одна из выше перечисленных команд не была отловлена при обновлении, выводится "не правильная команда"
        } else {
            SendMessage messageText = new SendMessage();
            messageText.setChatId(update.getMessage().getChatId());
            messageText.setText("не правильная команда");
            try {
                execute(messageText);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * метод для создания/изменения сообщения
     * @param messageId Id сообщения не может быть null
     * @param chatIdInButton
     * @param messageText
     */
    private void changeMessage(int messageId, long chatIdInButton, String messageText) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatIdInButton));
        editMessageText.setText(messageText);
        editMessageText.setMessageId(messageId);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}