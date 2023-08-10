package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class GetPetReportButton {
    protected static final InlineKeyboardButton dailyReportFormButton = new InlineKeyboardButton("Форма ежедневного отчета");
    protected static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
    protected static final InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");


    public InlineKeyboardMarkup sendMessageReportFromPet() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = List.of(List.of(dailyReportFormButton), List.of(callVolunteerButton),List.of(toStart));
        keyboardMarkup.setKeyboard(rowsList);
        toStart.setCallbackData("В начало");
        dailyReportFormButton.setCallbackData("Форма ежедневного отчета");
        callVolunteerButton.setCallbackData("Позвать волонтера");
        return keyboardMarkup;
        return sendMessage;
    }

    // отправка формы отчета
    public SendMessage dailyReportForm(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Пришлите следующие данные в одном сообщении:\n" +
                "- *Фото животного.*\n" +
                "- *Рацион животного.*\n" +
                "- *Общее самочувствие и привыкание к новому месту.*\n" +
                "- *Изменение в поведении: отказ от старых привычек, приобретение новых.*");
        return sendMessage;
    }

    // проверка формы отчета
    public SendMessage dailyReportCheck(long chatId, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setChatId(chatId);

        if (update.getMessage().hasPhoto()) {
            sendMessage.setText("Отчет сохранен");
            //реализация сохранения отчета, если надо будет
        } else {
            sendMessage.setText("Ежедневный отчет отправлен не верно! Нет");
        }
        return sendMessage;
    }
}
