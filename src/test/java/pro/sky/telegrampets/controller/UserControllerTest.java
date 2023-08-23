package pro.sky.telegrampets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrampets.impl.UserServiceImpl;
import pro.sky.telegrampets.model.User;
import pro.sky.telegrampets.repository.UserRepository;

import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Slava", 3, "896500328");
        user.setId(1L);
    }

    @Test
    void testUserAdd() throws Exception {
        Mockito.when(userServiceImpl.userAdd(Mockito.any())).thenReturn(user);
        mockMvc.perform(post("/user/save").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andExpect(content().contentType("application/json"));
    }

    @Test
    void testGetUserByChatId() throws Exception {
        Mockito.when(userServiceImpl.getUserByChatId(anyInt())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/user/chatId/{chatId}", 3)).andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        User updateUser = new User("Slava", 4, "8965003");
        Mockito.when(userServiceImpl.updateUser(Mockito.any())).thenReturn(updateUser);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        mockMvc.perform(put("/user/update/{id}", 1).content(objectMapper.writeValueAsString(updateUser)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andExpect(content().contentType("application/json"));
    }

    @Test
    void testCheckIdChatUser() {
        Mockito.when(userServiceImpl.checkIdChatUser(anyInt())).thenReturn(true);
        int chatId = 3;
        boolean result = userServiceImpl.checkIdChatUser(chatId);
        assertTrue(result);
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.when(userServiceImpl.userAdd(Mockito.any())).thenReturn(user);
        long id = user.getId();
        mockMvc.perform(delete("/user/delete/{id}", id)).andExpect(status().isOk());
    }
}