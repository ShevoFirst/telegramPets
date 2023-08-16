package pro.sky.telegrampets.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;/*
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;*/
import pro.sky.telegrampets.model.User;
//import pro.sky.telegrampets.model.Volunteer;
import pro.sky.telegrampets.repository.UserRepository;
import pro.sky.telegrampets.service.UserService;

import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final Volunteer volunteer;
/*
    private final TelegramBot telegramBot;
*/

    public UserServiceImpl(UserRepository userRepository /*Volunteer volunteer/*, TelegramBot telegramBot*/) {
        this.userRepository = userRepository;
       // this.volunteer = volunteer;
/*
        this.telegramBot = telegramBot;
*/
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
        String telegramP = "String.valueOf(volunteer.getChatId());";
        /*sendMessageVolunteers.setParseMode(String.valueOf(ParseMode.HTML));*/
        return new SendMessage(telegramP, volunteerMessage);
    }

    //Добавить нового пользователя в БД
    @Override
    public User userAdd(User user) {
        return userRepository.save(user);
    }

    //Удалить пользователя из БД
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //Проверить, если пользователь в бд по его chatId
    @Override
    public boolean checkIdChatUser(int chatId) {
        return userRepository.existsByChatId(chatId);
    }

    //Обновить пользователя в бд
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    //Получить юзера из бд
    @Override
    public Optional<User> getUserByChatId(int chatId) {
        return userRepository.findByChatId(chatId);
    }


}
