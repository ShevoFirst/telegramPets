package pro.sky.telegrampets.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface UserService {
    SendMessage sendMessageToVolunteer(Long chatId);

}
