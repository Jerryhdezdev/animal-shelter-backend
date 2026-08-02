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
    private AnimalResponseDTO createTestResponseDTO(){
        AnimalResponseDTO responseDTO = new AnimalResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Max");
        responseDTO.setSpecies("DOG");
        responseDTO.setSex("MALE");
        responseDTO.setBirthDate(LocalDate.of(2026,1,1));
        responseDTO.setWeight(new BigDecimal("25.5"));
        responseDTO.setSize("LARGE");
        responseDTO.setVaccinationStatus("FULL_VACCINATED");
        responseDTO.setSterilizationStatus("STERILIZED");
        responseDTO.setAdoptionStatus("INTAKE_ASSESSMENT");
        responseDTO.setIntakeDate(LocalDate.now());
        responseDTO.setDescription("Max is a friendly dog");
        return responseDTO;
    }

    // Helper method - builds a test request DTO to reuse across tests
    private AnimalRequestDTO createTestRequestDTO(){
        AnimalRequestDTO requestDTO = new AnimalRequestDTO();
        requestDTO.setName("Max");
        requestDTO.setAnimalSpecies(AnimalSpecies.DOG);
        requestDTO.setAnimalSex(AnimalSex.MALE);
        requestDTO.setBirthDate(LocalDate.of(2026,1,1));
        requestDTO.setWeight(new BigDecimal("25.5"));
        requestDTO.setAnimalSize(AnimalSize.LARGE);
        requestDTO.setAnimalVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        requestDTO.setAnimalSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        requestDTO.setDescription("Max is a friendly dog");
        return requestDTO;
    }

    @Test
    void shouldReturnAllAnimals() throws Exception {
        // ARRANGE
        AnimalResponseDTO responseDTO = createTestResponseDTO();
        when(animalService.getAllAnimals()).thenReturn(List.of(responseDTO));

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
        AnimalResponseDTO responseDTO = createTestResponseDTO();
        when(animalService.getAnimalById(1L)).thenReturn(responseDTO);

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
        AnimalRequestDTO requestDTO =  createTestRequestDTO();
        AnimalResponseDTO responseDTO = createTestResponseDTO();
        String requestJson = objectMapper.writeValueAsString(requestDTO);
        doReturn(responseDTO).when(animalService).createAnimal(any(AnimalRequestDTO.class));

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
        AnimalRequestDTO requestDTO = createTestRequestDTO();
        AnimalResponseDTO responseDTO = createTestResponseDTO();
        responseDTO.setName("Max Update");

        doReturn(responseDTO).when(animalService).updateAnimal(eq(1L), any(AnimalRequestDTO.class));



        // ACT + ASSERT
        mockMvc.perform(put("/api/v1/animals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
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
