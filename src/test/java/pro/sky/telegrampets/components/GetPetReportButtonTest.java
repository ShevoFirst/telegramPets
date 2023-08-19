package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import pro.sky.telegrampets.impl.ReportServiceImpl;
import pro.sky.telegrampets.impl.UserServiceImpl;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


    @ExtendWith(MockitoExtension.class)
    public class GetPetReportButtonTest {

        @Mock
        private UserServiceImpl userService;

        @Mock
        private ReportServiceImpl reportService;

        @Mock
        private UserRepository userRepository;

        @Mock
        private ReportRepository reportRepository;

        @Test
        public void testSendMessageDailyReportPhoto() {
            GetPetReportButton button = new GetPetReportButton(userService, reportService, userRepository, reportRepository);
            SendMessage message = button.sendMessageDailyReportPhoto(123);
            assertEquals("123", message.getChatId());
            assertEquals("Пришлите фото питомца", message.getText());
        }

        @Test
        public void testDailyReportCheckPhotoWithPhoto() {
            GetPetReportButton button = new GetPetReportButton(userService, reportService, userRepository, reportRepository);
            Update update = mock(Update.class);
            when(update.getMessage().hasPhoto()).thenReturn(true);
            SendMessage message = button.dailyReportCheckPhoto(123, update);
            assertEquals("123", message.getChatId());
            assertEquals("Фото сохранено, пришлите текстовую часть отчета.", message.getText());
        }

        @Test
        public void testDailyReportCheckPhotoWithoutPhoto() {
            GetPetReportButton button = new GetPetReportButton(userService, reportService, userRepository, reportRepository);
            Update update = mock(Update.class);
            when(update.getMessage().hasPhoto()).thenReturn(false);
            SendMessage message = button.dailyReportCheckPhoto(123, update);
            assertEquals("123", message.getChatId());
            assertEquals("Вы прислали не фото!", message.getText());
        }

        @Test
        public void testDailyReportCheckMessageWithText() {
            GetPetReportButton button = new GetPetReportButton(userService, reportService, userRepository, reportRepository);
            Update update = mock(Update.class);
            when(update.getMessage().hasText()).thenReturn(true);
            SendMessage message = button.dailyReportCheckMessage(123, update, "photo-id");
            assertEquals("123", message.getChatId());
            assertEquals("Отчет сохранен", message.getText());

            verify(button).saveReportMessage(eq(update), eq("photo-id"));
        }

        @Test
        public void testDailyReportCheckMessageWithoutText() {
            GetPetReportButton button = new GetPetReportButton(userService, reportService, userRepository, reportRepository);
            Update update = mock(Update.class);
            when(update.getMessage().hasText()).thenReturn(false);
            SendMessage message = button.dailyReportCheckMessage(123, update, "photo-id");
            assertEquals("123", message.getChatId());
            assertEquals("Вы не прислали текстовую часть отчета!", message.getText());
        }

        @Test
        public void testSaveUserWhenUserExists() {
            // Arrange
            Update update = new Update();
            Message message = new Message();
            User user = new User();
            user.setChatId(123);
            update.setMessage(message);
            when(userService.getUserByChatId(123)).thenReturn(Optional.of(user));

            // Assert
            verify(userService, times(1)).updateUser(user);
        }

        @Test
        public void testSaveUserWhenUserDoesNotExist() {
            // Arrange
            Update update = new Update();
            Message message = new Message();
            update.setMessage(message);
            when(userService.getUserByChatId(123)).thenReturn(Optional.empty());

            // Assert
            verify(userService, times(1)).userAdd(any(User.class));
        }

        @Test
        public void testSaveReportMessage() {
            // Arrange
            Update update = new Update();
            Message message = new Message();
            message.setText("Some text");
            update.setMessage(message);
            Report report = new Report();
            report.setPhotoNameId("123");
            when(reportRepository.findReportByPhotoNameId("123")).thenReturn(Optional.of(report));

            // Assert
            verify(reportService, times(1)).updateReport(report);
        }

        @Test
        public void testSaveReportPhotoId() {
            // Arrange
            Update update = new Update();
            Message message = new Message();
            User user = new User();
            user.setChatId(123);
            update.setMessage(message);
            when(userRepository.findUserByChatId(123)).thenReturn(user);
            Report report = new Report();
            report.setPhotoNameId("123");
            when(reportService.reportAdd(report)).thenReturn(report);

        }
    }