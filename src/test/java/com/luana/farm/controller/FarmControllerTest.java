package com.luana.farm.controller;

import com.luana.farm.service.FarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FarmControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FarmService farmService;

    @InjectMocks
    private FarmController farmController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(farmController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createFarmShouldReturnCreatedFarm() throws Exception {
        String farmJson = """
            {
                "name": "Test Farm",
                "landArea": 100.0,
                "unitOfMeasure": "acre",
                "address": "123 Farm Lane",
                "productions": []
            }
        """;

        mockMvc.perform(post("/api/farms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(farmJson))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteFarmShouldReturnNoContent() throws Exception {
        doNothing().when(farmService).deleteFarm(1L);

        mockMvc.perform(delete("/api/farms/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listFarmsShouldReturnFarms() throws Exception {
        mockMvc.perform(get("/api/farms"))
                .andExpect(status().isOk());
    }
}
