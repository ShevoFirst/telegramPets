package pro.sky.telegrampets.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.UserRepository;

import java.util.Optional;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserAdd() {
        User user = new User();
        User savedUser = new User();
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.userAdd(user);

        assertEquals(savedUser, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        Long id = 123L;

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testCheckIdChatUser() {
        int chatId = 123;
        when(userRepository.existsByChatId(chatId)).thenReturn(true);

        boolean result = userService.checkIdChatUser(chatId);

        assertTrue(result);
        verify(userRepository, times(1)).existsByChatId(chatId);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserByChatId() {
        int chatId = 123;
        User user = new User();
        when(userRepository.findByChatId(chatId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByChatId(chatId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByChatId(chatId);
    }

}

