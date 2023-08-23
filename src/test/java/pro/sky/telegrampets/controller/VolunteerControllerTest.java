package pro.sky.telegrampets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import pro.sky.telegrampets.model.Volunteer;
import pro.sky.telegrampets.repository.VolunteerRepository;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

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
    @MockBean
    private VolunteerRepository volunteerRepository;

    private List<Volunteer> volunteers;

    @BeforeEach
    void setUp() {
        Volunteer volunteer = new Volunteer("Slava", "Safronov", 3);
        volunteer.setId(1L);
        Volunteer volunteer1 = new Volunteer("Slava2", "Safronov2", 4);
        volunteer1.setId(2L);
        volunteers = List.of(volunteer, volunteer1);
    }

    @Test
    void testDeleteVolunteerById() throws Exception {
        Mockito.when(volunteerRepository.save(Mockito.any())).thenReturn(volunteers.get(0));
        mockMvc.perform(delete("/volunteer/delete/{id}", 1)).andExpect(status().isOk());
    }

    @Test
    void testSave() throws Exception {
        Mockito.when(volunteerRepository.save(Mockito.any())).thenReturn(volunteers.get(0));
        mockMvc.perform(post("/volunteer/save").content(objectMapper.writeValueAsString(volunteers.get(0))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andExpect(content().contentType("application/json")).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Slava")).andExpect(jsonPath("$.lastName").value("Safronov")).andExpect(jsonPath("$.chatId").value(3L));
    }

    @Test
    void testAllVolunteer() throws Exception {
        Mockito.when(volunteerRepository.findAll()).thenReturn(volunteers);
        mockMvc.perform(get("/volunteer/list"))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.*", Matchers.hasSize(2))).andExpect(jsonPath("$[0].id").value(1L)).andExpect(jsonPath("$[0].name").value("Slava")).andExpect(jsonPath("$[0].lastName").value("Safronov")).andExpect(jsonPath("$[0].chatId").value(3L)).andExpect(jsonPath("$[1].id").value(2L)).andExpect(jsonPath("$[1].name").value("Slava2")).andExpect(jsonPath("$[1].lastName").value("Safronov2")).andExpect(jsonPath("$[1].chatId").value(4L));
    }

    @Test
    void testFindByIdVolunteer() throws Exception {
        Mockito.when(volunteerRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(volunteers.get(0)));
        mockMvc.perform(get("/volunteer/read/{id}", 1L)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Slava")).andExpect(jsonPath("$.lastName").value("Safronov")).andExpect(jsonPath("$.chatId").value(3L));
    }

    @Test
    void testUpdateById() throws Exception {
        Volunteer updateVolunteer = new Volunteer(1L, "Slava3", "Safronov3", 5L);
        Mockito.when(volunteerRepository.save(Mockito.any())).thenReturn(updateVolunteer);
        Mockito.when(volunteerRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(volunteers.get(0)));
        mockMvc.perform(put("/volunteer/update/{id}", 1).content(objectMapper.writeValueAsString(volunteers.get(1))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Slava3")).andExpect(jsonPath("$.lastName").value("Safronov3")).andExpect(jsonPath("$.chatId").value(5L));
    }
}
