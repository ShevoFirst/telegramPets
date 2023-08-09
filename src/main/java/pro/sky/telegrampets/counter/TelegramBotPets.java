package pro.sky.telegrampets.counter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
    protected boolean isACatShelter;

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
        if (isStartCommand(update)) {
            long chatId = update.getMessage().getChatId();
            startSelection(chatId, update);
        } else if (update.hasCallbackQuery()) { //проверка на нажатие кнопки
            String callbackData = update.getCallbackQuery().getData();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                //Cтартовый блок
                case "Кошка" -> catSelection(messageId, chatId);
                case "Собака" -> dogSelection(messageId, chatId);

                //блок второго уровня
                case "Как взять животное из приюта?" -> takeAnimalSelection(messageId, chatId);
                case "Информация о приюте" -> shelterSelection(messageId, chatId);
                case "Позвать волонтера" -> callaVolunteer(messageId, chatId);
                case "Прислать отчет о питомце" -> petReportSelection(messageId, chatId);
                case "В начало" -> buttonToStart(messageId, chatId);

                //блок “Прислать отчет о питомце”
                case "Форма ежедневного отчета" -> {

                }

                //блок “Как взять животное из приюта”
                case "Обустройство котенка" -> KittenArrangementSelection(messageId, chatId);
                case "Обустройство щенка" -> puppyArrangementSelection(messageId, chatId);
                case "Правила знакомства" -> datingRulesSelection(messageId, chatId);
                case "Список документов" -> documentsSelection(messageId, chatId);
                case "Рекомендации по транспортировке" -> transportationSelection(messageId, chatId);
                case "Обустройство для взрослого" -> arrangementAdultSelection(messageId, chatId);
                case "Обустройство для ограниченного" -> arrangementLimitedSelection(messageId, chatId);
                case "Cписок причин" -> listReasonsSelection(messageId, chatId);
                case "Запись контактов" -> recordingContactsSelection(messageId, chatId);
            }
        }else{
            handleInvalidCommand(update.getMessage().getChatId());
        }
    }

    private void recordingContactsSelection(int messageId, long chatId) {
    }

    private void listReasonsSelection(int messageId, long chatId) {
    }

    private void arrangementLimitedSelection(int messageId, long chatId) {
    }

    private void arrangementAdultSelection(int messageId, long chatId) {
    }

    private void transportationSelection(int messageId, long chatId) {
    }

    private void documentsSelection(int messageId, long chatId) {
    }

    private void datingRulesSelection(int messageId, long chatId) {
    }

    private void puppyArrangementSelection(int messageId, long chatId) {
    }

    private void KittenArrangementSelection(int messageId, long chatId) {
    }

    private boolean isStartCommand(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start");
    }

    //метод кнопки "Как взять животное из приюта?"
    private void takeAnimalSelection(int messageId, long chatId) {
        String messageText = "Выберите что вас интересует";
        InlineKeyboardMarkup catsButtons = buttons.takeAnimalButton(isACatShelter);
        changeMessage(messageId, chatId, messageText, catsButtons);
    }

    //метод кнопки "Позвать волонтера"
    private void callaVolunteer(int messageId, long chatId) {
    }


    //метод кнопки "Информация о приюте"
    private void shelterSelection(int messageId, long chatId) {
    }

    private void startSelection(long chatId, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(new Buttons().selectionAnimalButtons());
        sendMessage.setText("Привет! " + update.getMessage().getFrom().getFirstName() + " Выберите приют который Вас интересует:");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void buttonToStart(int messageId, long chatId) {
        String messageText = " Выберите приют который Вас интересует:";
        changeMessage(messageId, chatId,messageText , new Buttons().selectionAnimalButtons());
    }

    private void catSelection(int messageId, long chatId) {
        isACatShelter = true;
        String messageText = "Вы выбрали приют для кошек";
        InlineKeyboardMarkup catsButtons = buttons.secondLayerButtons();
        changeMessage(messageId, chatId, messageText, catsButtons);
    }

    private void dogSelection(int messageId, long chatId) {
        isACatShelter = false;
        InlineKeyboardMarkup dogButtons = buttons.secondLayerButtons();
        changeMessage(messageId,chatId,"вы выбрали собачий приют",dogButtons);
    }

    //метод кнопки "Прислать отчет о питомце"
    private void petReportSelection(int messageId, long chatId) {
        InlineKeyboardMarkup reportButtons = getPetReportButton.sendMessageReportFromPet();
        changeMessage(messageId,chatId,"Выберите одну из кнопок",reportButtons);
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
    private void changeMessage(int messageId, long chatIdInButton, String messageText,InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatIdInButton));
        editMessageText.setText(messageText);
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(keyboardMarkup);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
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