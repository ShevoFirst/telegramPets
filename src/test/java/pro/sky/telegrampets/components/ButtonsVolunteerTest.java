package pro.sky.telegrampets.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import pro.sky.telegrampets.impl.ReportServiceImpl;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.repository.ReportRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ButtonsVolunteerTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportServiceImpl reportService;

    public ButtonsVolunteerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testButtonVolunteer() {
        // Arrange
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(reportRepository, reportService);

        // Act
        InlineKeyboardMarkup markupInline = buttonsVolunteer.buttonVolunteer();

        // Assert
        Assertions.assertNotNull(markupInline);
        List<List<InlineKeyboardButton>> rowsList = markupInline.getKeyboard();
        Assertions.assertEquals(2, rowsList.size());
        List<InlineKeyboardButton> firstRow = rowsList.get(0);
        Assertions.assertEquals(1, firstRow.size());
        InlineKeyboardButton listOfReports = firstRow.get(0);
        Assertions.assertEquals("Просмотр отчетов", listOfReports.getText());
        Assertions.assertEquals("Отчеты", listOfReports.getCallbackData());
        List<InlineKeyboardButton> secondRow = rowsList.get(1);
        Assertions.assertEquals(1, secondRow.size());
        InlineKeyboardButton toStart = secondRow.get(0);
        Assertions.assertEquals("В начало", toStart.getText());
        Assertions.assertEquals("В начало", toStart.getCallbackData());
    }

    @Test
    public void testReviewListOfReports_NoReports() {
        // Arrange
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(reportRepository, reportService);
        Long chatId = 123456789L;
        when(reportRepository.findReportByCheckReportIsFalse()).thenReturn(new ArrayList<>());

        // Act
        SendMessage message = buttonsVolunteer.reviewListOfReports(chatId);

        // Assert
        Assertions.assertNotNull(message);
        Assertions.assertEquals(chatId.toString(), message.getChatId());
        Assertions.assertEquals("Нет непроверенных отчетов.", message.getText());
    }

    @Test
    public void testReviewListOfReports_WithReports() {
        // Arrange
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(reportRepository, reportService);
        Long chatId = 123456789L;
        Report report = new Report();
        report.setId(1L);
        report.setGeneralWellBeing("Test report");
        List<Report> reportList = new ArrayList<>();
        reportList.add(report);
        when(reportRepository.findReportByCheckReportIsFalse()).thenReturn(reportList);

        // Act
        SendMessage message = buttonsVolunteer.reviewListOfReports(chatId);

        // Assert
        Assertions.assertNotNull(message);
        Assertions.assertEquals(chatId.toString(), message.getChatId());
        String expectedText = """
                Отчет #1
                Текстовая часть отчета: Test report
                Фото отчета -\s""";
        Assertions.assertEquals(expectedText, message.getText());
        InlineKeyboardMarkup markupInline = (InlineKeyboardMarkup) message.getReplyMarkup();
        Assertions.assertNotNull(markupInline);
        List<List<InlineKeyboardButton>> rowsList = markupInline.getKeyboard();
        Assertions.assertEquals(1, rowsList.size());
        List<InlineKeyboardButton> row = rowsList.get(0);
        Assertions.assertEquals(2, row.size());
        InlineKeyboardButton button1 = row.get(0);
        Assertions.assertEquals("Отчет сдан", button1.getText());
        Assertions.assertEquals("ОТЧЕТ СДАН", button1.getCallbackData());
        InlineKeyboardButton button2 = row.get(1);
        Assertions.assertEquals("Отчет не сдан", button2.getText());
        Assertions.assertEquals("Флаг кнопки 2 для отчета", button2.getCallbackData());
    }

    @Test
    public void testReportSubmitted() {
        // Arrange
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(reportRepository, reportService);
        Long idReport = 1L;
        Report report = new Report();
        report.setId(idReport);
        report.setCheckReport(false);
        when(reportRepository.findReportById(idReport)).thenReturn(report);

        // Act
        buttonsVolunteer.reportSubmitted(idReport);

        // Assert
        verify(reportService, times(1)).updateReport(report);
        Assertions.assertTrue(report.isCheckReport());
    }

    @Test
    public void testParseReportNumber() {
        // Arrange
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(reportRepository, reportService);
        String reportString = "Отчет #123\nТекстовая часть отчета: Test report";

        // Act
        int reportNumber = buttonsVolunteer.parseReportNumber(reportString);

        // Assert
        Assertions.assertEquals(123, reportNumber);
    }

    @Test
    public void testParseReportNumber_NoMatch() {
        // Arrange
        ButtonsVolunteer buttonsVolunteer = new ButtonsVolunteer(reportRepository, reportService);
        String reportString = "Текстовая часть отчета: Test report";

        // Act
        int reportNumber = buttonsVolunteer.parseReportNumber(reportString);

        // Assert
        Assertions.assertEquals(-1, reportNumber);
    }
}