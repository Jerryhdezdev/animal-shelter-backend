package com.jerryhdez.animalshelter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.service.AdoptionService;
import com.jerryhdez.animalshelter.exception.AdoptionNotFoundException;
import com.jerryhdez.animalshelter.web.dto.AdoptionRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AdoptionResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdoptionController.class)



public class AdoptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdoptionService adoptionService;


    // Helpers
    private AdoptionResponseDTO createTestResponseDTO(){
        AdoptionResponseDTO responseDTO = new AdoptionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setAnimalName("Fiona");
        responseDTO.setUserName("Jerry Hdez");
        responseDTO.setAdoptionStatus("REQUESTED");
        return responseDTO;
    }

    private AdoptionRequestDTO createTestRequestDTO(){
        AdoptionRequestDTO requestDTO = new AdoptionRequestDTO();
        requestDTO.setAnimalId(1L);
        return requestDTO;
    }

    @Test
    void shouldReturnAllAdoptions() throws Exception {
        //  ARRANGE
        AdoptionResponseDTO responseDTO = createTestResponseDTO();
        when(adoptionService.getAllAdoptions()).thenReturn(List.of(responseDTO));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/adoptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animalName").value("Fiona"))
                .andExpect(jsonPath("$[0].userName").value("Jerry Hdez"));
        verify(adoptionService, times(1)).getAllAdoptions();
    }

    @Test
    void shouldReturnAdoptionById() throws Exception {
        // ARRANGE
        AdoptionResponseDTO responseDTO = createTestResponseDTO();
        when(adoptionService.getAdoptionById(1L)).thenReturn(responseDTO);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/adoptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.animalName").value("Fiona"));
        verify(adoptionService, times(1)).getAdoptionById(1L);
    }

    @Test
    void shouldReturn404WhenAdoptionNotFound() throws Exception {
        // ARRANGE
        when(adoptionService.getAdoptionById(13L))
                .thenThrow(new AdoptionNotFoundException(13L));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/adoptions/13"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
        verify(adoptionService, times(1)).getAdoptionById(13L);
    }

    @Test
    void shouldCreateAdoption() throws Exception {
        // ARRANGE
        AdoptionResponseDTO responseDTO = createTestResponseDTO();
        AdoptionRequestDTO requestDTO = createTestRequestDTO();

        String requestJson = objectMapper.writeValueAsString(requestDTO);

        doReturn(responseDTO).when(adoptionService).createAdoption(any(), any());

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.animalName").value("Fiona"))
                .andExpect(jsonPath("$.userName").value("Jerry Hdez"));

        verify(adoptionService, times(1)).createAdoption(any(), any());
    }

    @Test
    void shouldUpdateAdoption() throws Exception {
        // ARRANGE
        AdoptionResponseDTO responseDTO = createTestResponseDTO();
        responseDTO.setAdoptionStatus("APPROVED");


        when(adoptionService.updateAdoption(eq(1L), eq(AdoptionStatus.APPROVED))).thenReturn(responseDTO);

        // ACT + ASSERT
        mockMvc.perform(patch("/api/v1/adoptions/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("newStatus", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adoptionStatus").value("APPROVED"));
        verify(adoptionService, times(1)).updateAdoption(eq(1L), eq(AdoptionStatus.APPROVED));
    }

    @Test
    void shouldDeleteAdoption() throws Exception {
        // ARRANGE
        doNothing().when(adoptionService).deleteAdoption(1L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/adoptions/1"))
                .andExpect(status().isNoContent());
        verify(adoptionService, times(1)).deleteAdoption(1L);
    }

    @Test
    void shouldReturn404WhenDeletingAdoptionNotFound() throws Exception {
        // ARRANGE
        doThrow(new AdoptionNotFoundException(13L))
                .when(adoptionService).deleteAdoption(13L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/adoptions/13"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
        verify(adoptionService, times(1)).deleteAdoption(13L);
    }

}
