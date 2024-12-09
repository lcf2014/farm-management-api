package com.luana.farm.integration;

import com.luana.farm.entities.Farm;
import com.luana.farm.repository.FarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FarmIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private FarmRepository farmRepository;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build();
    }

    @Test
    void createAndListFarmShouldWorkCorrectly() throws Exception {
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
                        .contentType("application/json")
                        .content(farmJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/farms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Farm"));

        mockMvc.perform(delete("/api/farms/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFarmShouldRemoveFarm() throws Exception {
        Farm farm = Farm.builder().
                name("Farm to Delete").
                landArea(50.0).
                unitOfMeasure("acre").
                address("123 Delete Lane").
                build();

        farm = farmRepository.save(farm);

        mockMvc.perform(delete("/api/farms/" + farm.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/farms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }
}
