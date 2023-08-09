package pro.sky.telegrampets.counter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.components.GetPetReportButton;
import pro.sky.telegrampets.config.TelegramBotConfiguration;

/**
 * класс реагирущий на реакции бота через telegram api
 */
@Component
public class TelegramBotPets extends TelegramLongPollingBot {
    //подключение конфигуратора к нашему боту
    private final TelegramBotConfiguration telegramBotConfiguration;
    private final Buttons buttons;
    private final GetPetReportButton getPetReportButton;

    public TelegramBotPets(TelegramBotConfiguration telegramBotConfiguration, Buttons buttons, GetPetReportButton getPetReportButton) {
        this.telegramBotConfiguration = telegramBotConfiguration;
        this.buttons = buttons;
        this.getPetReportButton = getPetReportButton;
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
     *
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {
        //в случае если пользователь введет команду "/start" выведутся кнопки 1ого уровня
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            long chatId = update.getMessage().getChatId();
            startSelection(chatId, update);
            //в случае если пользователь нажал на одну из кнопок то они заменятся на текст и выведутся кнопки 2ого уровня
        } else if (update.hasCallbackQuery()) { //проверка на передачу нажатия кнопки
            String callbackData = update.getCallbackQuery().getData(); // название CallbackData кнопки
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "Кошка" -> catSelection(messageId, chatId);
                case "Собака" -> dogSelection(messageId, chatId);
            }
            //в случае если пользователь нажал на одну из кнопок то они заменятся на текст и выведутся кнопки 3ого уровня
            if (update.hasCallbackQuery()) {
                String callbackData2 = update.getCallbackQuery().getData();
                long messageId2 = update.getCallbackQuery().getMessage().getMessageId();
                long chatId2 = update.getCallbackQuery().getMessage().getChatId();
                switch (callbackData2) {
                    case "Как взять животное из приюта?" -> takeAnimalSelection(messageId2, chatId2);
                    case "Информация о приюте" -> shelterSelection(messageId2, chatId2);
                    case "Позвать волонтера" -> callaVolunteer(messageId2, chatId2);
                    case "Прислать отчет о питомце" -> petReportSelection(messageId2, chatId2);
                }
            } else {
                handleInvalidCommand(chatId);
            }
        }
    }

    private void petReportSelection(long messageId, long chatId) {

        SendMessage sendMessage = getPetReportButton.sendMessageReportFromPet(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //метод кнопки "Как взять животное из приюта?"
    private void takeAnimalSelection(long messageId2, long chatId2) {
    }

    //метод кнопки "Позвать волонтера"
    private void callaVolunteer(long messageId2, long chatId2) {
    }


    //метод кнопки "Информация о приюте"
    private void shelterSelection(long messageId2, long chatId2) {
    }

    private void startSelection(long chatId, Update update) {
        SendMessage sendMessage = buttons.selectionAnimalButtons(chatId, update);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void catSelection(long messageId, long chatId) {
        String messageText = "Вы выбрали приют для кошек";
        changeMessage((int) messageId, chatId, messageText);
        SendMessage catsButtons = buttons.secondLayerButtons(chatId);
        try {
            execute(catsButtons);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void dogSelection(long messageId, long chatId) {
        String textCat = "Вы выбрали приют для собак";
        changeMessage((int) messageId, chatId, textCat);
        SendMessage dogButtons = buttons.secondLayerButtons(chatId);
        try {
            execute(dogButtons);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleInvalidCommand(long chatId) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText("не правильная команда");
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод для создания/изменения сообщения
     *
     * @param messageId      Id сообщения не может быть null
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