package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


import static org.junit.jupiter.api.Assertions.*;

class ButtonsTest {


    @Test
    public void testSelectionAnimalButtons() {
        Buttons buttons = new Buttons();
        Update update = new Update();
        Message message = new Message();
        User user = new User();
        user.setFirstName("Дмитрий");
        message.setFrom(user);
        update.setMessage(message);

        InlineKeyboardMarkup result = buttons.selectionAnimalButtons();

        assertEquals("Дмитрий", update.getMessage().getFrom().getFirstName());
        assertEquals("Привет! Дмитрий Выберите приют который Вас интересует:", result.getKeyboard());
      //  assertEquals("Кошка", Buttons.catButton.getCallbackData());
      //  assertEquals("Собака", Buttons.dogButton.getCallbackData());

    }

    @Test
    void secondLayerButtons() {

        Update update = new Update();
        Message message = new Message();
        User user = new User();
        user.setFirstName("Дмитрий");
        message.setFrom(user);
        update.setMessage(message);

       // assertEquals("Информация о приюте", Buttons.checkInfoButton.getCallbackData());
       // assertEquals("Позвать волонтера", Buttons.callVolunteerButton.getCallbackData());
        //assertEquals("Прислать отчет о питомце", Buttons.getReportAboutPet.getCallbackData());
       // assertEquals("Как взять животное из приюта?", Buttons.howGetPet.getCallbackData());
    }
}