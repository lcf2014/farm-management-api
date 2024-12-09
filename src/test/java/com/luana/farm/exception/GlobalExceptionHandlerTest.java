package com.luana.farm.exception;

import com.luana.farm.controller.FarmController;
import com.luana.farm.service.FarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private FarmService farmService;

    @InjectMocks
    private FarmController farmController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(farmController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Add the global exception handler
                .build();
    }

    @Test
    void handleFarmNotFoundExceptionShouldReturn404() throws Exception {
        doThrow(new FarmNotFoundException("Farm with ID 999 not found"))
                .when(farmService).deleteFarm(999L);

        mockMvc.perform(delete("/api/farms/999"))
                .andExpect(status().isNotFound()) // Expecting 404 status
                .andExpect(jsonPath("$.status").value(404)) // Validate status in response body
                .andExpect(jsonPath("$.message").value("Farm with ID 999 not found")) // Validate error message
                .andExpect(jsonPath("$.errors").isEmpty()); // Validate empty errors list
    }

    @Test
    void handleValidationExceptionShouldReturn400() throws Exception {
        String invalidFarmJson = """
            {
                "landArea": -50.0,
                "unitOfMeasure": "",
                "address": "123 Farm Lane",
                "productions": []
            }
        """;

        mockMvc.perform(post("/api/farms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFarmJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400)) // Expecting the 'status' field
                .andExpect(jsonPath("$.message").value("Validation error")) // Expecting the 'message' field
                .andExpect(jsonPath("$.errors").isArray()) // Expecting the 'errors' field to be an array
                .andExpect(jsonPath("$.errors", hasSize(3))) // Expecting 3 validation errors
                .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("landArea", "unitOfMeasure", "name"))) // Check the fields
                .andExpect(jsonPath("$.errors[*].message").value(containsInAnyOrder(
                        "Land area must be greater than 0",
                        "Unit of measure is required",
                        "Name is required"
                )));
    }

    @Test
    void handleGenericExceptionShouldReturn500() throws Exception {
        doThrow(new RuntimeException("Unexpected error"))
                .when(farmService).deleteFarm(1L);

        mockMvc.perform(delete("/api/farms/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }
}
