package pro.sky.telegrampets.counter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramTest {
    @Mock
    TelegramBotPets telegramBotPets;
    @Captor
    private ArgumentCaptor<SendMessage> messageArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> chatId;
    @Captor
    private ArgumentCaptor<String> photoIdByReport;

    @Test
    @DisplayName("Отправка фото")
    void sendImageFromFileId() throws TelegramApiException {
        telegramBotPets.sendImageFromFileId("123321", "123");
        verify(telegramBotPets, times(1)).sendImageFromFileId(photoIdByReport.capture(), chatId.capture());

        var photoIdByReportValue = photoIdByReport.getValue();
        assertEquals(photoIdByReportValue, "123321");
        var chatIdValue = chatId.getValue();
        assertEquals(chatIdValue, "123");
        assertDoesNotThrow(() -> telegramBotPets.executeSendMessage(any(SendMessage.class)));

    }

    SendMessage sendMessage = new SendMessage();


    {
        sendMessage.setChatId(123L);
        sendMessage.setText("qwe");
    }

    @Test
    @DisplayName("Отправка сообщения")
    void executeSendMessage() {
        telegramBotPets.executeSendMessage(sendMessage);
        verify(telegramBotPets, times(1)).executeSendMessage(messageArgumentCaptor.capture());

        var value = messageArgumentCaptor.getValue();

        assertEquals(value.getText(), "qwe");
        assertEquals(value.getChatId(), "123");
    }

    SendPhoto sendPhoto = new SendPhoto();


    {
        sendPhoto.setChatId("123");
        sendPhoto.setPhoto(new InputFile("123321"));
    }


    @Captor
    private ArgumentCaptor<Update> messageText;

    @Test
    @DisplayName("Если отчет сдан")
    void reportSubmitted() {
        Message message = new Message();
        message.setText("Отчет сдан");

        Update update = new Update();
        update.setMessage(message);

        telegramBotPets.reportSubmitted(update);
        verify(telegramBotPets, times(1)).reportSubmitted(messageText.capture());

        var messageTextValue = messageText.getValue();
        assertEquals(messageTextValue.getMessage().getText(), "Отчет сдан");
    }

    @Captor
    private ArgumentCaptor<Update> reportNotSubmittedCaptor;

    @Test
    @DisplayName("Если отчет не сдан")
    void reportNotSubmitted() {
        Message message = new Message();
        message.setText("Если отчет не сдан");

        Update update = new Update();
        update.setMessage(message);

        telegramBotPets.reportNotSubmitted(update);
        verify(telegramBotPets, times(1)).reportNotSubmitted(reportNotSubmittedCaptor.capture());
        assertEquals(reportNotSubmittedCaptor.getValue().getMessage().getText(), "Если отчет не сдан");
    }

    @Captor
    private ArgumentCaptor<Long> reviewListOfReports;

    @Test
    @DisplayName("Отправляем текст непроверенного отчета, а так же получаем ID фото")
    void reviewListOfReports() {
        telegramBotPets.reviewListOfReports(123L);

        verify(telegramBotPets, times(1)).reviewListOfReports(reviewListOfReports.capture());
        var reviewListOfReportsValue = reviewListOfReports.getValue();
        assertEquals(reviewListOfReportsValue, 123L);
    }
}
