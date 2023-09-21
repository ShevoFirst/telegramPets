package pro.sky.telegrampets.timer;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.sky.telegrampets.counter.TelegramBotPets;
import pro.sky.telegrampets.model.Report;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.UserRepository;

@ContextConfiguration(classes = {NotificationTaskTimer.class})
@ExtendWith(SpringExtension.class)
class NotificationTaskTimerTest {
    @Autowired
    private NotificationTaskTimer notificationTaskTimer;

    @MockBean
    private TelegramBotPets telegramBotPets;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link NotificationTaskTimer#task()}
     */
    @Test
    void testTask() {
        User user = new User();
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByDateTimeToTookBefore(Mockito.<LocalDateTime>any())).thenReturn(ofResult);
        notificationTaskTimer.task();
        verify(userRepository).findByDateTimeToTookBefore(Mockito.<LocalDateTime>any());
    }

    /**
     * Method under test: {@link NotificationTaskTimer#task()}
     */
    @Test
    void testTask2() {
        User user = mock(User.class);
        when(user.getDateTimeToTook()).thenReturn(LocalDate.now().atStartOfDay());
        doNothing().when(user).setChatId(anyInt());
        doNothing().when(user).setDateTimeToTook(Mockito.<LocalDateTime>any());
        doNothing().when(user).setFirstName(Mockito.<String>any());
        doNothing().when(user).setId(anyLong());
        doNothing().when(user).setNumber(Mockito.<String>any());
        doNothing().when(user).setReports(Mockito.<List<Report>>any());
        doNothing().when(user).setTookAPet(Mockito.<Boolean>any());
        user.setChatId(1);
        user.setDateTimeToTook(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setFirstName("Jane");
        user.setId(1L);
        user.setNumber("42");
        user.setReports(new ArrayList<>());
        user.setTookAPet(true);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByDateTimeToTookBefore(Mockito.any())).thenReturn(ofResult);
        notificationTaskTimer.task();
        verify(userRepository).findByDateTimeToTookBefore(Mockito.any());
        verify(user).getDateTimeToTook();
        verify(user).setChatId(anyInt());
        verify(user).setDateTimeToTook(Mockito.any());
        verify(user).setFirstName(Mockito.any());
        verify(user).setId(anyLong());
        verify(user).setNumber(Mockito.any());
        verify(user).setReports(Mockito.any());
        verify(user).setTookAPet(Mockito.<Boolean>any());
    }

    /**
     * Method under test: {@link NotificationTaskTimer#task()}
     */
    @Test
    void testTask3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByDateTimeToTookBefore(Mockito.<LocalDateTime>any())).thenReturn(emptyResult);
        notificationTaskTimer.task();
        verify(userRepository).findByDateTimeToTookBefore(Mockito.<LocalDateTime>any());
    }
}

