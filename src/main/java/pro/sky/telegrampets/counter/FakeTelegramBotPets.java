package pro.sky.telegrampets.counter;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.components.ButtonsVolunteer;
import pro.sky.telegrampets.components.GetPetReportButton;
import pro.sky.telegrampets.config.TelegramBotConfiguration;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;
import pro.sky.telegrampets.repository.VolunteerRepository;

import java.io.Serializable;

public class FakeTelegramBotPets extends TelegramBotPets {
    public FakeTelegramBotPets(TelegramBotConfiguration telegramBotConfiguration, ReportRepository reportRepository, Buttons buttons, GetPetReportButton getPetReportButton, UserRepository userRepository, ButtonsVolunteer buttonsVolunteer, VolunteerRepository volunteerRepository) {
        super(telegramBotConfiguration, reportRepository, buttons, getPetReportButton, userRepository, buttonsVolunteer, volunteerRepository);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        // Ничего не делаем, чтобы избежать ошибки
        return null;
    }
}
