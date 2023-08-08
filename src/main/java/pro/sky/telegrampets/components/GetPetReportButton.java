package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class GetPetReportButton {
    protected static final InlineKeyboardButton dailyReportFormButton = new InlineKeyboardButton("Форма ежедневного отчета");
    protected static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");

    public SendMessage sendMessageReportFromPet(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберите одну из кнопок");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = List.of(List.of(dailyReportFormButton), List.of(callVolunteerButton));
        keyboardMarkup.setKeyboard(rowsList);
        sendMessage.setReplyMarkup(keyboardMarkup);
        dailyReportFormButton.setCallbackData("Форма ежедневного отчета");
        callVolunteerButton.setCallbackData("Позвать волонтера");

        return sendMessage;
    }
}
