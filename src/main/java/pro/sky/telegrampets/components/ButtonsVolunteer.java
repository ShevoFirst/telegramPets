package pro.sky.telegrampets.components;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
public class ButtonsVolunteer {
    public SendMessage ButtonVolunteer() {
        SendMessage message = new SendMessage();
        message.setChatId(931733272L);
        message.setText("ВОЛОНТЕР");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton volunteer = new InlineKeyboardButton("Кнопка для волонтера");
        InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
        List<List<InlineKeyboardButton>> rowsList = List.of(List.of(volunteer), List.of(toStart));

        markupInline.setKeyboard(rowsList);
        message.setReplyMarkup(markupInline);
        return message;
    }
}
