package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class Buttons {
    private static final InlineKeyboardButton CAT_BUTTON = new InlineKeyboardButton("Приют Кошек \uD83D\uDC08");
    private static final InlineKeyboardButton DOG_BUTTON = new InlineKeyboardButton("Приют Собак \uD83E\uDDAE");

    public SendMessage selectionAnimalButtons(long chatId, Update update) {
        String userFirstName = update.getMessage().getFrom().getFirstName();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Привет! " + userFirstName + " Выберите приют который Вас интересует:");

        CAT_BUTTON.setCallbackData("Кошка");
        DOG_BUTTON.setCallbackData("Собака");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtons = List.of(CAT_BUTTON, DOG_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(keyboardButtons);

        keyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }
}
