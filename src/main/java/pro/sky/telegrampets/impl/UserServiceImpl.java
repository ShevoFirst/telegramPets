package pro.sky.telegrampets.impl;
import org.springframework.stereotype.Component;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.UserRepository;

import java.util.Optional;

@Component
public class UserServiceImpl {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Добавить нового пользователя в БД
    public User userAdd(User user) {
        return userRepository.save(user);
    }

    //Удалить пользователя из БД
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //Проверить, если пользователь в бд по его chatId
    public boolean checkIdChatUser(int chatId) {
        return userRepository.existsByChatId(chatId);
    }

    //Обновить пользователя в бд
    public void updateUser(User user) {
        userRepository.save(user);
    }

    //Получить юзера из бд
    public Optional<User> getUserByChatId(int chatId) {
        return userRepository.findByChatId(chatId);
    }


}
