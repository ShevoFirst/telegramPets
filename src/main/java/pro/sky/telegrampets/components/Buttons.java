package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * class for creating first and second level buttons
 */

@Component
public class Buttons {

    // InlineKeyBoardButton поле text это то что будет написано на кнопке
    protected static final InlineKeyboardButton catButton = new InlineKeyboardButton("Приют Кошек \uD83D\uDC08");
    protected static final InlineKeyboardButton dogButton = new InlineKeyboardButton("Приют Собак \uD83E\uDDAE");
    protected static final InlineKeyboardButton checkInfoButton = new InlineKeyboardButton("Информация о приюте");
    protected static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
    protected static final InlineKeyboardButton getReportAboutPet = new InlineKeyboardButton("Прислать отчет о питомце");
    protected static final InlineKeyboardButton howGetPet = new InlineKeyboardButton("Как взять животное из приюта?");
    protected static final InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");

    /**
     * @return returns dog button and cat button
     */
    /*
    SendMessage отправляет сообщение пользователю
    InlineKeyboardButton линейные кнопки под прикрепленным сообщением
    ReplyKeyboardMarkup кнопки внутри интерфейса телеграмма при нажатии которых возвращается определенный текст
     */
    public InlineKeyboardMarkup selectionAnimalButtons() {
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
        return keyboardMarkup;
    }

    /**
     * @return returns dog button and cat button
     */

    //аналогичный методу первого уровня за исключением того что кнопок 4 и находятся они друг под другом
    public InlineKeyboardMarkup secondLayerButtons() {
        checkInfoButton.setCallbackData("Информация о приюте");
        callVolunteerButton.setCallbackData("Позвать волонтера");
        getReportAboutPet.setCallbackData("Прислать отчет о питомце");
        howGetPet.setCallbackData("Как взять животное из приюта?");
        toStart.setCallbackData("В начало");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = List.of(List.of(checkInfoButton), List.of(callVolunteerButton), List.of(getReportAboutPet), List.of(howGetPet),List.of(toStart));
        keyboardMarkup.setKeyboard(rowsInLine);
        return keyboardMarkup;
    }

}
