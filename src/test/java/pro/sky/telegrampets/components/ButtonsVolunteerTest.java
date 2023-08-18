package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ButtonsVolunteerTest {
    @Test
    public void testButtonVolunteer() {
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(); // замените MyClass на имя вашего класса
        InlineKeyboardMarkup markupInline = buttonsVolunteer.ButtonVolunteer();

        assertEquals(2, markupInline.getKeyboard().size());
        assertEquals("Кнопка для волонтера", markupInline.getKeyboard().get(0).get(0).getText());
        assertEquals("Волонтер", markupInline.getKeyboard().get(0).get(0).getCallbackData());

        assertEquals("В начало", markupInline.getKeyboard().get(1).get(0).getText());
        assertEquals("В начало", markupInline.getKeyboard().get(1).get(0).getCallbackData());
    }
}
