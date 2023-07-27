package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class Buttons {
    private static final InlineKeyboardButton catButton = new InlineKeyboardButton("Приют Кошек \uD83D\uDC08");
    private static final InlineKeyboardButton dogButton = new InlineKeyboardButton("Приют Собак \uD83E\uDDAE");
    private static final InlineKeyboardButton checkInfoButton = new InlineKeyboardButton("Информация о приюте");
    private static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
    private static final InlineKeyboardButton getReportAboutPet = new InlineKeyboardButton("Прислать отчет о питомце");
    private static final InlineKeyboardButton howGetPet = new InlineKeyboardButton("Как взять животное из приюта?");

    public SendMessage selectionAnimalButtons(long chatId, Update update) {
        String userFirstName = update.getMessage().getFrom().getFirstName();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Привет! " + userFirstName + " Выберите приют который Вас интересует:");
        catButton.setCallbackData("Кошка");
        dogButton.setCallbackData("Собака");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtons = List.of(catButton, dogButton);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(keyboardButtons);
        keyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }
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
