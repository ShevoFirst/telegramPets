package pro.sky.telegrampets.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.model.Volunteer;
import pro.sky.telegrampets.repository.UserRepository;
import pro.sky.telegrampets.service.UserService;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Volunteer volunteer;
    private final TelegramBot telegramBot;

    public UserServiceImpl(UserRepository userRepository, Volunteer volunteer, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.volunteer = volunteer;
        this.telegramBot = telegramBot;
    }

    @Override
    public SendMessage sendMessageToVolunteer(Long chatId) {
        String text = "пивет волонтер нужна твоя помощь";
        User user = userRepository.getUserByChatId(chatId).get();
        String volunteerMessage = String.format("""
                        <b>%s</b> позвать волонтера
                        номер id пользователя: %s 
                        имя пользователя: %s
                         Прекрепленое сообщение: %s""",
                user.getId(),
                user.getFirstName(),
                text);
        String telegramP = String.valueOf(volunteer.getChatId());
        SendMessage sendMessageVolunteers = new SendMessage(telegramP, volunteerMessage);
        sendMessageVolunteers.setParseMode(String.valueOf(ParseMode.HTML));
        return sendMessageVolunteers;
    }
}