package pro.sky.telegrampets.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.service.UserService;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void sendMessageToVolunteerTest() {
MockitoAnnotations.initMocks(this);
        Long chatId = 1L;
        User user = new User();
        user.setFirstName("Slava");
        user.setId(1L);
        userService.sendMessageToVolunteer(chatId);
        Assertions.assertEquals(1l, user.getId());
        Assertions.assertEquals("Slava", user.getFirstName());
    }
}
