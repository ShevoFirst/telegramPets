package pro.sky.telegrampets.timer;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrampets.counter.TelegramBotPets;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.UserRepository;

public class NotificationTaskTimerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TelegramBotPets telegramBotPets;

    @InjectMocks
    private NotificationTaskTimer notificationTaskTimer;

    @Test
    public void testTask() {
        MockitoAnnotations.openMocks(this);

        LocalDateTime oneDaysAgo = LocalDateTime.of(2023,10,3,0,0);
        User user = new User();
        user.setDateTimeToTook(oneDaysAgo.minusDays(2)); // User took the pets more than 2 days ago
        user.setChatId(1);

        notificationTaskTimer.task();

        when(userRepository.findByDateTimeToTookBefore(oneDaysAgo)).thenReturn(Optional.of(user));

        verify(userRepository, times(1)).findByDateTimeToTookBefore(oneDaysAgo);
        verify(telegramBotPets, times(1)).changeMessage(user.getChatId(), " «Дорогой усыновитель, мы заметили, что ты заполняешь " +
                "отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее" +
                " к этому занятию. В противном случае волонтеры приюта будут обязаны " +
                "самолично проверять условия содержания животного»");
    }
}