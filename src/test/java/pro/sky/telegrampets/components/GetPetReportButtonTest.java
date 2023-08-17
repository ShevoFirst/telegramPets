package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPetReportButtonTest {

    @Test
    void sendMessageReportFromPet() {
        // Создаем моки для InlineKeyboardButton
        InlineKeyboardButton dailyReportFormButton = mock(InlineKeyboardButton.class);
        InlineKeyboardButton callVolunteerButton = mock(InlineKeyboardButton.class);
        InlineKeyboardButton toStart = mock(InlineKeyboardButton.class);
        
    }

    @Test
    void dailyReportForm() {
    }

    @Test
    void dailyReportCheck() {
    }

    @Test
    void saveUser() {
    }
}