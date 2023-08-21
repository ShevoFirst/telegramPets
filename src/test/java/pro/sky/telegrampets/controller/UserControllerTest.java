package pro.sky.telegrampets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrampets.impl.UserServiceImpl;
import pro.sky.telegrampets.model.User;


import javax.ws.rs.core.MediaType;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserServiceImpl userService;


    @Test
    void deleteUser() throws Exception {
        User user = new User(1L, "Slava", 15, 896500328);
        long id = user.getId();
        mockMvc.perform(delete("/user/delete/{id}", id))
                .andExpect(status().isOk());
    }
    @Test
    void userAdd() throws Exception {
        User user = new User( 1L,"Slava", 3,896500328);
        mockMvc.perform(post("/user/save")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Slava"))
                .andExpect(jsonPath("$.chatId").value(3))
                .andExpect(jsonPath("$.number").value(896500328));

    }
    @Test
    void getUserByChatId() throws Exception {
        mockMvc.perform(get("/user/chatId/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Slava"))
                .andExpect(jsonPath("$.chatId").value(3))
                .andExpect(jsonPath("$.number").value(896500328));
    }
    @Test
    void updateUser() throws Exception {
        User user = new User( 1L,"Slava", 4,8965003);
        long id = user.getId();
        mockMvc.perform(put("/user/update/{id}",id)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Slava"))
                .andExpect(jsonPath("$.chatId").value(4))
                .andExpect(jsonPath("$.number").value(8965003));
    }

    @Test
    void checkIdChatUser() {
        int chatId = 4;
        boolean result = userService.checkIdChatUser(chatId);
        assertEquals(true,result);

    }
}