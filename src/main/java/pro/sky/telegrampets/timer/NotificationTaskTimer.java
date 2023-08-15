package pro.sky.telegrampets.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrampets.counter.TelegramBotPets;
import pro.sky.telegrampets.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class NotificationTaskTimer {
    private final UserRepository userRepository;


    public NotificationTaskTimer(UserRepository userRepository, TelegramBotPets telegramBotPets) {
        this.userRepository = userRepository;

    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public List<Integer> task() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusSeconds(10);
        System.out.println("TEST");
        List<Integer> chatIds = new ArrayList<>(); //
        userRepository.findAllByDateTimeToTook(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .forEach(user -> {
                    if (user.getDateTimeToTook().isBefore(twoDaysAgo)) {
                        chatIds.add(user.getChatId()); // Добавляем chatId в список
                    }
                });
        return chatIds;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void TEST_ONE() {
        System.out.println("TEST");
    }
}

