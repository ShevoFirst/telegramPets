package pro.sky.telegrampets.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.impl.ReportServiceImpl;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.model.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Кнопки для волонтера и проверка отчетов питомцев от пользователей
 */
@Component
public class ButtonsVolunteer {
    private final ReportRepository reportRepository;
    private final ReportServiceImpl reportServiceImpl;


    public ButtonsVolunteer(ReportRepository reportRepository, ReportServiceImpl reportServiceImpl) {
        this.reportRepository = reportRepository;
        this.reportServiceImpl = reportServiceImpl;
    }

    /**
     * Вызов волонтерской панели
     */
    public InlineKeyboardMarkup buttonVolunteer() {
        InlineKeyboardButton listOfReports = new InlineKeyboardButton("Просмотр отчетов");
        InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");
        listOfReports.setCallbackData("Отчеты");
        toStart.setCallbackData("В начало");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = List.of(List.of(listOfReports), List.of(toStart));
        markupInline.setKeyboard(rowsList);
        return markupInline;
    }

    /**
     * Получаем непроверенный отчет из всех отчетов
     *
     * @return возвращает первый непроверенный отчет и кнопки действия с отчетом
     */
    public SendMessage reviewListOfReports(Long chatId) {
        List<Report> reportList = reportRepository.findReportByCheckReportIsFalse();
        if (reportList.isEmpty()) {
            SendMessage noReportsMessage = new SendMessage();
            noReportsMessage.setChatId(chatId);
            noReportsMessage.setText("Нет непроверенных отчетов.");
            return noReportsMessage;
        } else {
            for (Report report : reportList) {
                SendMessage reportMessage = new SendMessage();
                reportMessage.setChatId(chatId);


                String reportInfo = "Отчет #" + report.getId() + "\n" +
                        "Текстовая часть отчета: " + report.getGeneralWellBeing() +
                        "\n" + "Фото отчета - ";
                reportMessage.setText(reportInfo);

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

                InlineKeyboardButton button1 = new InlineKeyboardButton("Отчет сдан");
                InlineKeyboardButton button2 = new InlineKeyboardButton("Отчет не сдан");
                button1.setCallbackData("ОТЧЕТ СДАН");
                button2.setCallbackData("Флаг кнопки 2 для отчета");
                rowsInLine.add(List.of(button1, button2));

                keyboardMarkup.setKeyboard(rowsInLine);
                reportMessage.setReplyMarkup(keyboardMarkup);

                return reportMessage;
            }
        }
        return null;
    }

    /**
     * Обновляем в БД отчет и ставим, что отчет сдан
     */
    public void reportSubmitted(Long idReport) {
        Report report = reportRepository.findReportById(idReport);
        report.setCheckReport(true);
        reportServiceImpl.updateReport(report);
    }


    /**
     * Позволяет распарсить SendMessage из метода reviewListOfReports что-бы достать ID репорта
     * с которым будем работать.
     *
     * @param reportString получаем строку SendMessage
     * @return вовзращает ID отчета
     */
    public int parseReportNumber(String reportString) {
        Pattern pattern = Pattern.compile("Отчет #(\\d+)");
        Matcher matcher = pattern.matcher(reportString);

        if (matcher.find()) {
            String numberStr = matcher.group(1);
            return Integer.parseInt(numberStr);
        }
        return -1; // В случае, если не удалось извлечь номер отчета
    }


}