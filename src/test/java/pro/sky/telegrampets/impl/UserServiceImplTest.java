package pro.sky.telegrampets.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.UserRepository;
import pro.sky.telegrampets.service.UserService;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void sendMessageToVolunteerTest() {
MockitoAnnotations.initMocks(this);
        Long chatId = 1L;
        User user = new User();
        user.setFirstName("Slava");
        user.setNumber(896500328);
        userService.sendMessageToVolunteer(chatId);
        Assertions.assertEquals(896500328, user.getNumber());
        Assertions.assertEquals("Slava", user.getFirstName());
    }
}
