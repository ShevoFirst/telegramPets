package pro.sky.telegrampets.components;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
public class ButtonsVolunteer {
    public InlineKeyboardMarkup ButtonVolunteer() {

        InlineKeyboardButton volunteer = new InlineKeyboardButton("Кнопка для волонтера");
        InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
        volunteer.setCallbackData("Волонтер");
        toStart.setCallbackData("В начало");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = List.of(List.of(volunteer), List.of(toStart));
        markupInline.setKeyboard(rowsList);
        return markupInline;
    }
}
