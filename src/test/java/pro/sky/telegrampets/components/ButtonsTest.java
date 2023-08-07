package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ButtonsTest {


    @Test
    public void testSelectionAnimalButtons() {
        Buttons buttons = new Buttons();
        long chatId = 123456789;
        Update update = new Update();
        Message message = new Message();
        User user = new User();
        user.setFirstName("Дмитрий");
        message.setFrom(user);
        update.setMessage(message);

        SendMessage result = buttons.selectionAnimalButtons(chatId, update);

        assertEquals(String.valueOf(chatId), result.getChatId());
        assertEquals("Дмитрий", update.getMessage().getFrom().getFirstName());
        assertEquals("Привет! Дмитрий Выберите приют который Вас интересует:", result.getText());
        assertEquals("Кошка", Buttons.catButton.getCallbackData());
        assertEquals("Собака", Buttons.dogButton.getCallbackData());

        InlineKeyboardMarkup markup = (InlineKeyboardMarkup) result.getReplyMarkup();
        List<List<InlineKeyboardButton>> keyboard = markup.getKeyboard();
        assertEquals(1, keyboard.size());
        assertEquals(2, keyboard.get(0).size());

    }

    @Test
    void secondLayerButtons() {
        Buttons buttons = new Buttons();
        long chatId = 123456789;
        Update update = new Update();
        Message message = new Message();
        User user = new User();
        user.setFirstName("Дмитрий");
        message.setFrom(user);
        update.setMessage(message);

        SendMessage result = buttons.secondLayerButtons(chatId);
        assertEquals("Информация о приюте", Buttons.checkInfoButton.getCallbackData());
        assertEquals("Позвать волонтера", Buttons.callVolunteerButton.getCallbackData());
        assertEquals("Прислать отчет о питомце", Buttons.getReportAboutPet.getCallbackData());
        assertEquals("Как взять животное из приюта?", Buttons.howGetPet.getCallbackData());
    }
}