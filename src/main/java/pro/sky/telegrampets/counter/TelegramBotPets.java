package pro.sky.telegrampets.counter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
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
        if (isStartCommand(update)) {
            long chatId = update.getMessage().getChatId();
            startSelection(chatId, update);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "Кошка" -> catSelection(messageId, chatId);
                case "Собака" -> dogSelection(messageId, chatId);
                case "Как взять животное из приюта?" -> takeAnimalSelection(messageId, chatId);
                case "Информация о приюте" -> shelterSelection(messageId, chatId);
                case "Позвать волонтера" -> callaVolunteer(messageId, chatId);
                case "Прислать отчет о питомце" -> petReportSelection(messageId, chatId, update);
                case "Форма ежедневного отчета" -> takeDailyReportForm(messageId, chatId, update);
            }
        } else if (update.hasMessage()) {
            checkDailyReport(update);
        }
    }

    private void checkDailyReport(Update update) {
        long chatId = update.getMessage().getChatId();
        executeSendMessage(getPetReportButton.dailyReportCheck(chatId, update));
    }


    //проверка на /start
    private boolean isStartCommand(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start");
    }

    //вызов Форма ежедневного отчета
    private void takeDailyReportForm(long messageId, long chatId, Update update) {
        SendMessage sendMessage = getPetReportButton.dailyReportForm(chatId);
        executeSendMessage(sendMessage);
    }


    //метод кнопки "Прислать отчет о питомце"
    private void petReportSelection(long messageId, long chatId, Update update) {
        SendMessage sendMessage = getPetReportButton.sendMessageReportFromPet(chatId);
        executeSendMessage(sendMessage);
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
        executeSendMessage(sendMessage);
    }

    private void catSelection(long messageId, long chatId) {
        String messageText = "Вы выбрали приют для кошек";
        changeMessage((int) messageId, chatId, messageText);
        SendMessage catsButtons = buttons.secondLayerButtons(chatId);
        executeSendMessage(catsButtons);
    }

    private void dogSelection(long messageId, long chatId) {
        String textCat = "Вы выбрали приют для собак";
        changeMessage((int) messageId, chatId, textCat);
        SendMessage dogButtons = buttons.secondLayerButtons(chatId);
        executeSendMessage(dogButtons);
    }


    private void handleInvalidCommand(long chatId) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText("не правильная команда");
        executeSendMessage(messageText);
    }

    private void executeSendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
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