package com.luana.farm.service;

import com.luana.farm.dto.SimplePage;
import com.luana.farm.entities.Farm;
import com.luana.farm.exception.FarmNotFoundException;
import com.luana.farm.repository.FarmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class FarmServiceTest {

    @InjectMocks
    private FarmService farmService;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private Logger mockLogger;

    @Test
    void createFarmShouldSaveAndLogFarm() {
        Farm farm = Farm.builder().
                name("Test Farm").
                landArea(100.0).
                build();

        when(farmRepository.save(farm)).thenReturn(farm);

        Farm savedFarm = farmService.createFarm(farm);

        assertNotNull(savedFarm);
        assertEquals("Test Farm", savedFarm.getName());
        verify(farmRepository, times(1)).save(farm);

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, times(1)).debug(logCaptor.capture(), eq(farm));

        String loggedMessage = logCaptor.getValue();
        assertTrue(loggedMessage.contains("Saving a new farm:"), "Log message should indicate farm saving.");
    }

    @Test
    void deleteFarmShouldLogAndDeleteWhenFarmExists() {
        Long farmId = 1L;

        when(farmRepository.existsById(farmId)).thenReturn(true);

        farmService.deleteFarm(farmId);

        verify(farmRepository, times(1)).deleteById(farmId);

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, times(1)).debug(logCaptor.capture(), eq(farmId));

        String loggedMessage = logCaptor.getValue();
        assertTrue(loggedMessage.contains("Deleting farm with ID:"), "Log message should indicate farm deletion.");
    }

    @Test
    void deleteFarmShouldLogAndThrowExceptionWhenFarmDoesNotExist() {
        Long farmId = 999L;

        when(farmRepository.existsById(farmId)).thenReturn(false);

        FarmNotFoundException exception = assertThrows(
                FarmNotFoundException.class,
                () -> farmService.deleteFarm(farmId)
        );

        assertEquals("Farm with id " + farmId + " not found.", exception.getMessage());
        verify(farmRepository, never()).deleteById(farmId);
    }

    @Test
    void listFarmsShouldLogAndReturnPaginatedFarms() {
        Farm farm = Farm.builder().
                name("Test Farm").
                build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Farm> farmPage = new PageImpl<>(Collections.singletonList(farm), pageable, 1);

        when(farmRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(farmPage);

        SimplePage<Farm> result = farmService.listFarms("Corn", 50.0, 150.0, pageable);

        assertNotNull(result);
        assertEquals(1, result.getPageable().getTotalElements());
        assertEquals("Test Farm", result.getContent().get(0).getName());
        verify(farmRepository, times(1)).findAll(any(Specification.class), eq(pageable));

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, atLeastOnce()).debug(logCaptor.capture());
        assertTrue(logCaptor.getValue().contains("Searching farms with filters."));
    }

    @Test
    void listFarmsShouldLogAndReturnEmptyPageWhenNoFarmsMatch() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Farm> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(farmRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        SimplePage<Farm> result = farmService.listFarms("Wheat", 100.0, 200.0, pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(farmRepository, times(1)).findAll(any(Specification.class), eq(pageable));

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, atLeastOnce()).debug(logCaptor.capture());
        assertTrue(logCaptor.getValue().contains("Searching farms with filters."));
    }
}
