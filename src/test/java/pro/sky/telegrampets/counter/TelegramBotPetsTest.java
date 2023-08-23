package pro.sky.telegrampets.counter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.*;
import pro.sky.telegrampets.config.*;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.repository.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

class TelegramBotPetsTest {

    @Mock
    private AbsSender mockAbsSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getBotUsername() {
    }

    @Test
    void getBotToken() {
    }

    @Test
    void onUpdateReceived() {
    }

    @Test
    void sendImageFromFileId() throws TelegramApiException {

    }


    @Test
    public void testCheckDailyReportPhoto() {
    }

    @Test
    void changeMessage() {
    }

    @Test
    void testChangeMessage() {
    }

    @Test
    void callAVolunteer() {
    }

    @Test
    void getPhoto() {
    }
}