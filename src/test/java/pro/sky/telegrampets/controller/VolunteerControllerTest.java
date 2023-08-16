package pro.sky.telegrampets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import pro.sky.telegrampets.model.Volunteer;



import javax.ws.rs.core.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VolunteerControllerTest {
    @Autowired
   private MockMvc mockMvc;
    @Autowired
   private ObjectMapper objectMapper;


    @Test
    void deleteVolunteerById() throws Exception {
        Volunteer volunteer = new Volunteer(10L,"Slava1", "Safronov", 2L);
        long id = volunteer.getId();
        mockMvc.perform(delete("/volunteer/delete/{id}",id))
                .andExpect(status().isOk());
    }
    @Test
    void save() throws Exception {
        Volunteer volunteer = new Volunteer( 17L,"Slava2", "Safronov2", 3L);
        mockMvc.perform(post("/volunteer/save")
                        .content(objectMapper.writeValueAsString(volunteer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("17"))
                .andExpect(jsonPath("$.name").value("Slava2"))
                .andExpect(jsonPath("$.lastName").value("Safronov2"))
                .andExpect(jsonPath("$.chatId").value(3L));

    }

    @Test
    void allVolunteer() throws Exception {
        mockMvc.perform(get("/volunteer/list"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*", Matchers.hasSize(7)))
                .andExpect(jsonPath("$[5].name").value("Slava1"))
                .andExpect(jsonPath("$[5].lastName").value("Safronov1"))
                .andExpect(jsonPath("$[5].chatId").value(2L))
                .andExpect(jsonPath("$[6].name").value("Slava2"))
                .andExpect(jsonPath("$[6].lastName").value("Safronov2"))
                .andExpect(jsonPath("$[6].chatId").value(3L));
    }

    @Test
    void findByIdVolunteer() throws Exception {
        mockMvc.perform(get("/volunteer/read/16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("16"))
                .andExpect(jsonPath("$.name").value("Sasha"))
                .andExpect(jsonPath("$.lastName").value("Oganesyan"))
                .andExpect(jsonPath("$.chatId").value(4L));
    }

    @Test
    void updateById() throws Exception {
      Volunteer updateVolunteer = new Volunteer(16L, "Sasha", "Oganesyan", 4L);
        long id = updateVolunteer.getId();
        mockMvc.perform(put("/volunteer/update/{id}",id)
                .content(objectMapper.writeValueAsString(updateVolunteer))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
               // .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sasha"))
                .andExpect(jsonPath("$.lastName").value("Oganesyan"))
                .andExpect(jsonPath("$.chatId").value(4L));
    }
}
