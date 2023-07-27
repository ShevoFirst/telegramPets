package pro.sky.telegrampets.components;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.List;

/**
 * class for creating first and second level buttons
 */
@Component
public class Buttons {
    // InlineKeyBoardButton поле text это то что будет написано на кнопке
    private static final InlineKeyboardButton catButton = new InlineKeyboardButton("Приют Кошек \uD83D\uDC08");
    private static final InlineKeyboardButton dogButton = new InlineKeyboardButton("Приют Собак \uD83E\uDDAE");
    private static final InlineKeyboardButton checkInfoButton = new InlineKeyboardButton("Информация о приюте");
    private static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
    private static final InlineKeyboardButton getReportAboutPet = new InlineKeyboardButton("Прислать отчет о питомце");
    private static final InlineKeyboardButton howGetPet = new InlineKeyboardButton("Как взять животное из приюта?");

    /**
     *
     * @param update called when /start is received
     * @param chatId chat id
     * @return returns dog button and cat button
     */
    /*
    SendMessage отправляет сообщение пользователю
    InlineKeyboardButton линейные кнопки под прикрепленным сообщением
    ReplyKeyboardMarkup кнопки внутри интерфейса телеграмма при нажатии которых возвращается определенный текст
     */
    public SendMessage selectionAnimalButtons(long chatId, Update update) {
        String userFirstName = update.getMessage().getFrom().getFirstName();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Привет! " + userFirstName + " Выберите приют который Вас интересует:");
        //присваивание "бирок" которые будут возвращатся при нажатии
        catButton.setCallbackData("Кошка");
        dogButton.setCallbackData("Собака");
        //Разметка кнопок
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtons = List.of(catButton, dogButton);
        //Для добавления кнопок в одну линию стоит в массив добавить только один массив со всеми кнопками
        List<List<InlineKeyboardButton>> rowsInLine = List.of(keyboardButtons);
        //Добавление в разметку массив с кнопками
        keyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }
    /**
     *
     * @param chatId chat id
     * @return returns dog button and cat button
     */

    //аналогичный методу первого уровня за исключением того что кнопки находятся друг под другом
    public SendMessage secondLayerButtons(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберите одну из кнопок");
        checkInfoButton.setCallbackData("Информация о приюте");
        callVolunteerButton.setCallbackData("Позвать волонтера");
        getReportAboutPet.setCallbackData("Прислать отчет о питомце");
        howGetPet.setCallbackData("Как взять животное из приюта?");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = List.of(List.of(checkInfoButton),List.of(callVolunteerButton),List.of(getReportAboutPet),List.of(howGetPet));
        keyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
