package pro.sky.telegrampets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;



import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pro.sky.telegrampets.model.Volunteer;
import pro.sky.telegrampets.repository.VolunteerRepository;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    private final VolunteerRepository volunteerRepository;

    public VolunteerController(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Operation(summary = "Внести волонтера",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Волонтер внесен в таблицу",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )),
                    @ApiResponse(responseCode = "500",
                            description = "Ошибка сервера",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Некорректный формат",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            ))})

    @PostMapping("/save")
    public Volunteer save(@RequestBody Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Operation(summary = "Изменить данные о волонтере",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Волонтер изменен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )),
                    @ApiResponse(responseCode = "500",
                            description = "Ошибка сервера",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Некорректный формат",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            ))})
    @PutMapping("/update/{id}")
    public ResponseEntity<Volunteer> updateById(@RequestBody Volunteer volunteer) {
        Volunteer volunteer1 = volunteerRepository.save(volunteer);
        if (volunteer1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer1);
    }

    @Operation(summary = "Получение волонтера по id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Волонтер найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )),
                    @ApiResponse(responseCode = "500",
                            description = "Ошибка сервера",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Волонтер не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            ))
            })

    @GetMapping("/read/{id}")
    public ResponseEntity<Volunteer> findByIdVolunteer(@PathVariable @Positive Long id) {
        return ResponseEntity.of(volunteerRepository.findById(id));
    }

    @Operation(summary = "Удаления волонтера по id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Волонтер удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )),
                    @ApiResponse(responseCode = "500",
                            description = "Ошибка сервера",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Нет волонтера с таким id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            ))
            })
    @DeleteMapping("/delete/{id}")
    public void deleteVolunteerById(@PathVariable Long id) {
        volunteerRepository.deleteById(id);
    }


    @Operation(summary = "список волонтера ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "список волонтеров",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  array = @ArraySchema(schema = @Schema(implementation = Volunteer.class))
                            )),
                    @ApiResponse(responseCode = "500",
                            description = "Ошибка сервера",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema (schema = @Schema(implementation = Volunteer.class))

                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Нет волонтера в списке",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)

                            ))
            })
    @GetMapping("/list")
    public ResponseEntity<List<Volunteer>> allVolunteer() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        if (volunteers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteers);
    }
}
