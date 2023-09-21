package pro.sky.telegrampets.counter;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrampets.components.Buttons;
import pro.sky.telegrampets.components.ButtonsVolunteer;
import pro.sky.telegrampets.components.GetPetReportButton;
import pro.sky.telegrampets.config.TelegramBotConfiguration;
import pro.sky.telegrampets.repository.ReportRepository;
import pro.sky.telegrampets.repository.UserRepository;
import pro.sky.telegrampets.repository.VolunteerRepository;

import java.io.Serializable;
import java.util.function.Function;

public class FakeTelegramBotPets extends TelegramBotPets {
    private Function<BotApiMethod<?>, Object> executeFunction;
    private Object fakeSendPhotoResult;

    public FakeTelegramBotPets(TelegramBotConfiguration telegramBotConfiguration, ReportRepository reportRepository, Buttons buttons, GetPetReportButton getPetReportButton, UserRepository userRepository, ButtonsVolunteer buttonsVolunteer, VolunteerRepository volunteerRepository) {
        super(telegramBotConfiguration, reportRepository, buttons, getPetReportButton, userRepository, buttonsVolunteer, volunteerRepository);
    }
    // Добавьте метод для установки функции-заглушки
    public void setExecuteFunction(Function<BotApiMethod<?>, Object> executeFunction) {
        this.executeFunction = executeFunction;
    }

    // Добавьте метод для установки фейкового результата execute
    public void setFakeSendPhotoResult(Object fakeSendPhotoResult) {
        this.fakeSendPhotoResult = fakeSendPhotoResult;
    }
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        // Если установлена функция-заглушка, вызовите её, иначе верните null
        if (executeFunction != null) {
            return (T) executeFunction.apply(method);
        } else {
            return null;
        }
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Message capture) {

            return null;

    }
}
