package pro.sky.telegrampets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import pro.sky.telegrampets.model.Volunteer;
import pro.sky.telegrampets.repository.VolunteerRepository;

import javax.ws.rs.core.MediaType;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VolunteerControllerTest {
    @MockBean
    private VolunteerRepository volunteerRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void save() throws Exception {
        Volunteer volunteer = new Volunteer(99999L, "Slava", "Safronov",2L);
        mockMvc.perform(post("/volunteer/save")
                        .content(objectMapper.writeValueAsString(volunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(99999L))
                .andExpect(jsonPath("$.name").value("Slava"))
                .andExpect(jsonPath("$.lastName").value("Safronov"))
                .andExpect(jsonPath("$.chatId").value(2L));
    }
}
