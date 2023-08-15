package pro.sky.telegrampets.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrampets.counter.TelegramBotPets;
import pro.sky.telegrampets.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


public class NotificationTaskTimer {
    private final UserRepository userRepository;
    private final TelegramBotPets telegramBotPets;

    public NotificationTaskTimer(UserRepository userRepository, TelegramBotPets telegramBotPets) {
        this.userRepository = userRepository;
        this.telegramBotPets = telegramBotPets;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public int task() {

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusSeconds(10);
        String message = "НЕТ СООБЩЕНИЙ 10 секунд!!!";
        System.out.println("TEST");
        userRepository.findAllByDateTimeToTook(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .forEach(user -> {
                    int chatId = 0;
                    if (user.getDateTimeToTook().isBefore(twoDaysAgo)) {
                        telegramBotPets.changeMessage(user.getChatId(), message);
                        return user.getChatId();
                    }
                });
        return 0;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void TEST_ONE() {
        System.out.println("TEST");
    }
}

