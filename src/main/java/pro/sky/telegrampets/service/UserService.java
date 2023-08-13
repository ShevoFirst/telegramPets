package pro.sky.telegrampets.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import pro.sky.telegrampets.model.User;

public interface UserService {
    SendMessage sendMessageToVolunteer(Long chatId);

    User userAdd(User user);

    void deleteUser(Long id);


}
