package com.jerryhdez.animalshelter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerryhdez.animalshelter.domain.enums.AnimalSex;
import com.jerryhdez.animalshelter.domain.enums.AnimalSize;
import com.jerryhdez.animalshelter.domain.enums.AnimalSpecies;
import com.jerryhdez.animalshelter.domain.enums.AnimalSterilizationStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalVaccinationStatus;
import com.jerryhdez.animalshelter.domain.service.AnimalService;
import com.jerryhdez.animalshelter.exception.AnimalNotFoundException;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)

class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnimalService animalService;


    // Helper method - builds a test response DTO to reuse across tests
    private AnimalResponseDTO buildTestResponseDTO(){
        AnimalResponseDTO response = new AnimalResponseDTO();
        response.setId(1L);
        response.setName("Max");
        response.setSpecies("DOG");
        response.setSex("MALE");
        response.setBirthDate(LocalDate.of(2026,1,1));
        response.setWeight(new BigDecimal("25.5"));
        response.setSize("LARGE");
        response.setVaccinationStatus("FULL_VACCINATED");
        response.setSterilizationStatus("STERILIZED");
        response.setAdoptionStatus("INTAKE_ASSESSMENT");
        response.setIntakeDate(LocalDate.now());
        response.setDescription("Max is a friendly dog");
        return response;
    }

    // Helper method - builds a test request DTO to reuse across tests
    private AnimalRequestDTO buildTestRequestDTO(){
        AnimalRequestDTO request = new AnimalRequestDTO();
        request.setName("Max");
        request.setAnimalSpecies(AnimalSpecies.DOG);
        request.setAnimalSex(AnimalSex.MALE);
        request.setBirthDate(LocalDate.of(2026,1,1));
        request.setWeight(new BigDecimal("25.5"));
        request.setAnimalSize(AnimalSize.LARGE);
        request.setAnimalVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        request.setAnimalSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        request.setDescription("Max is a friendly dog");
        return request;
    }

    @Test
    void shouldReturnAllAnimals() throws Exception {
        // ARRANGE
        AnimalResponseDTO response = buildTestResponseDTO();
        when(animalService.getAllAnimals()).thenReturn(List.of(response));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Max"))
                .andExpect(jsonPath("$[0].species").value("DOG"));
        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    void shouldReturnAnimalById() throws Exception{
        // ARRANGE
        AnimalResponseDTO response = buildTestResponseDTO();
        when(animalService.getAnimalById(1L)).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/animals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Max"));
        verify(animalService, times(1)).getAnimalById(1L);
    }

    @Test
    void shouldReturn404WhenAnimalNotFound() throws Exception{
        // ARRANGE
        when(animalService.getAnimalById(13L))
                .thenThrow(new AnimalNotFoundException(13L));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/animals/13"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
        verify(animalService, times(1)).getAnimalById(13L);
    }

    @Test
    void shouldCreateAnimal() throws Exception{
        //ARRANGE
        AnimalRequestDTO request = buildTestRequestDTO();
        AnimalResponseDTO response = buildTestResponseDTO();
        String requestJson = objectMapper.writeValueAsString(request);
        doReturn(response).when(animalService).createAnimal(any(AnimalRequestDTO.class));

        //ACT + ASSERT
        mockMvc.perform(post("/api/v1/animals" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Max"));
        verify(animalService, times(1)).createAnimal(any(AnimalRequestDTO.class));
    }

    @Test
    void shouldUpdateAnimal() throws Exception{
        // ARRANGE
        AnimalRequestDTO request = buildTestRequestDTO();
        AnimalResponseDTO response = buildTestResponseDTO();
        response.setName("Max Update");

        doReturn(response).when(animalService).updateAnimal(eq(1L), any(AnimalRequestDTO.class));



        // ACT + ASSERT
        mockMvc.perform(put("/api/v1/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Max Update"));
        verify(animalService, times(1)).updateAnimal(eq(1L), any(AnimalRequestDTO.class));
    }

    @Test
    void shouldDeleteAnimal() throws Exception{
        // ARRANGE
        doNothing().when(animalService).deleteAnimal(1L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/animals/1"))
                .andExpect(status().isNoContent());
        verify(animalService, times(1)).deleteAnimal(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentAnimal() throws Exception{
        // ARRANGE
        doThrow(new AnimalNotFoundException(13L))
                .when(animalService).deleteAnimal(13L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/animals/13"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
        verify(animalService, times(1)).deleteAnimal(13L);
    }
}
