package pro.sky.telegrampets.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import pro.sky.telegrampets.impl.ReportServiceImpl;
import pro.sky.telegrampets.impl.UserServiceImpl;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.io.FileUtils.getFile;

@Service
public class GetPetReportButton {
    @Autowired
    private final UserServiceImpl userService;
    private final ReportServiceImpl reportService;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;


    protected static final InlineKeyboardButton dailyReportFormButton = new InlineKeyboardButton("Форма ежедневного отчета");
    protected static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
    protected static final InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");

    public GetPetReportButton(UserServiceImpl userService, ReportServiceImpl reportService, UserRepository userRepository, ReportRepository reportRepository) {
        this.userService = userService;
        this.reportService = reportService;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    public InlineKeyboardMarkup sendMessageReportFromPet() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = List.of(List.of(dailyReportFormButton), List.of(callVolunteerButton), List.of(toStart));
        keyboardMarkup.setKeyboard(rowsList);
        toStart.setCallbackData("В начало");
        dailyReportFormButton.setCallbackData("Форма ежедневного отчета");
        callVolunteerButton.setCallbackData("Позвать волонтера");
        return keyboardMarkup;
    }



    /**
     * Запрашива у пользователя фото
     *
     * @param chatId с пользователем
     * @return возвращаем сообщение
     */
    public SendMessage sendMessageDailyReportPhoto(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Пришлите фото питомца");
        return sendMessage;
    }

    /**
     * Проверяем, что пользователь прислал фото для отчета.
     */
    public SendMessage dailyReportCheckPhoto(long chatId, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setChatId(chatId);

        if (update.getMessage().hasPhoto()) {
            sendMessage.setText("Фото сохранено");
        } else {
            sendMessage.setText("Вы прислали не фото!");
        }
        return sendMessage;
    }

    /**
     * Проверяем, что пользователь прислал текст для отчета.
     */
    public SendMessage dailyReportCheckMessage(long chatId, Update update, String namePhotoId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setChatId(chatId);
        if (update.getMessage().hasText()) {
            sendMessage.setText("Отчет сохранен");
            saveReportMessage(update, namePhotoId);
        } else {
            sendMessage.setText("Вы не прислали текстовую часть отчета!");
        }
        return sendMessage;
    }

    /**
     * Поиск пользователя по chatId, если он есть то обновляем dateTimeToTook, если нет, создается новый пользователь
     */
    public void saveUser(Update update, boolean tookAPET) {
        int chatId = update.getMessage().getChatId().intValue();
        Optional<User> userOptional = userService.getUserByChatId(chatId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDateTimeToTook(LocalDateTime.now());
            userService.updateUser(user);
        } else {
            User newUser = new User();
            newUser.setFirstName(update.getMessage().getFrom().getFirstName());
            newUser.setChatId(chatId);
            newUser.setTookAPet(tookAPET);
            newUser.setDateTimeToTook(LocalDateTime.now());
            userService.userAdd(newUser);
        }
    }

    /**
     * Сохранение текствого отчет о питомце в БД
     */
    private void saveReportMessage(Update update, String namePhotoId) {
        Report report = reportRepository.findReportByPhotoNameId(namePhotoId);
        if (update.getMessage().hasText()) {
            report.setGeneralWellBeing(update.getMessage().getText());
        } else {
            report.setGeneralWellBeing("No text provided");
        }
        reportService.updateUser(report);
    }

    /**
     * Сохранение отчета в БД
     */
    public void saveReportPhotoId(Update update, String namePhotoId) {
        int chatId = update.getMessage().getChatId().intValue();
        Report report = new Report();
        report.setDateAdded(LocalDateTime.now());
        report.setPhotoNameId(namePhotoId);
        report.setGeneralWellBeing("No text provided");
        report.setUser(userRepository.findUserByChatId(chatId));
        reportService.reportAdd(report);
    }


}


