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
import pro.sky.telegrampets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.getFile;

@Service
public class GetPetReportButton {
    @Autowired
    private final UserServiceImpl userService;
    private final ReportServiceImpl reportService;
    private final UserRepository userRepository;
    protected static final InlineKeyboardButton dailyReportFormButton = new InlineKeyboardButton("Форма ежедневного отчета");
    protected static final InlineKeyboardButton callVolunteerButton = new InlineKeyboardButton("Позвать волонтера");
    protected static final InlineKeyboardButton toStart = new InlineKeyboardButton("В начало");

    public GetPetReportButton(UserServiceImpl userService, ReportServiceImpl reportService, UserRepository userRepository) {
        this.userService = userService;
        this.reportService = reportService;
        this.userRepository = userRepository;
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

    // отправка формы отчета
    public SendMessage dailyReportForm(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Пришлите следующие данные в одном сообщении:\n" +
                "- *Фото животного.*\n" +
                "- *Рацион животного.*\n" +
                "- *Общее самочувствие и привыкание к новому месту.*\n" +
                "- *Изменение в поведении: отказ от старых привычек, приобретение новых.*");
        return sendMessage;
    }

    /**
     * Проверяем отчет и сохроняем пользователя который его отправил
     */
    public SendMessage dailyReportCheck(long chatId, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setChatId(chatId);

        if (update.getMessage().hasPhoto()) {
            sendMessage.setText("Отчет сохранен");
            saveUser(update, true);
            saveReport(update);
        } else {
            sendMessage.setText("Ежедневный отчет отправлен не верно! Нет");
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
     * Сохранение отчета о питомце в БД
     */
    private void saveReport(Update update) {
        int chatId = update.getMessage().getChatId().intValue();
        Report report = new Report();
        report.setDateAdded(LocalDateTime.now());
        report.setGeneralWellBeing(update.getMessage().getText());
        report.setUser(userRepository.findUserByChatId(chatId));
        reportService.reportAdd(report);
    }
}


