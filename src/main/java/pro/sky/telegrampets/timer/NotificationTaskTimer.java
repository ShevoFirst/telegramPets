package pro.sky.telegrampets.timer;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrampets.counter.TelegramBotPets;
import pro.sky.telegrampets.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
public class NotificationTaskTimer {
    private final UserRepository userRepository;
    private final TelegramBotPets telegramBotPets;

    public NotificationTaskTimer(UserRepository userRepository, TelegramBotPets telegramBotPets) {
        this.userRepository = userRepository;
        this.telegramBotPets = telegramBotPets;
    }

    /**
     * Проверяет юзеров на отчеты, если отчета нет > 1 дня, то отправляет пользователю сообщение
     */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void task() {
        LocalDateTime oneDaysAgo = LocalDateTime.now().minusDays(1);
        String message = " «Дорогой усыновитель, мы заметили, что ты заполняешь " +
                "отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее" +
                " к этому занятию. В противном случае волонтеры приюта будут обязаны " +
                "самолично проверять условия содержания животного»";
        userRepository.findByDateTimeToTookBefore(oneDaysAgo)
                .ifPresent(user -> {
                    if (user.getDateTimeToTook().isBefore(oneDaysAgo)) {
                        telegramBotPets.changeMessage(user.getChatId(), message);
                    }
                });
    }
}

