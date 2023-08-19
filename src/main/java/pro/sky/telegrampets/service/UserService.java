package pro.sky.telegrampets.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import pro.sky.telegrampets.model.User;

import java.util.Optional;

public interface UserService {
    SendMessage sendMessageToVolunteer(Long chatId);

    User userAdd(User user);

    void deleteUser(Long id);

    boolean checkIdChatUser(int chatId);

    void updateUser(User user);

    Optional<User> getUserByChatId(int chatId);
}