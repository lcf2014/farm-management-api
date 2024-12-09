package com.luana.farm.service;

import com.luana.farm.dto.SimplePage;
import com.luana.farm.dto.SimplePageable;
import com.luana.farm.entities.Farm;
import com.luana.farm.exception.FarmNotFoundException;
import com.luana.farm.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository farmRepository;

    private final Logger logger;

    public Farm createFarm(final Farm farm) {
        logger.debug("Saving a new farm: {}", farm);
        return farmRepository.save(farm);
    }

    public void deleteFarm(final Long id) {
        if (!farmRepository.existsById(id)) {
            throw new FarmNotFoundException("Farm with id " + id + " not found.");
        }
        logger.debug("Deleting farm with ID: {}", id);
        farmRepository.deleteById(id);
    }

    public SimplePage<Farm> listFarms(final String cropType, final Double minLandArea, final Double maxLandArea, final Pageable pageable) {
        Specification<Farm> spec = Specification.where(null);

        if (cropType != null && !cropType.isBlank()) {
            spec = spec.and(FarmSpecifications.hasCropType(cropType));
        }

        if (minLandArea != null) {
            spec = spec.and(FarmSpecifications.hasMinLandArea(minLandArea));
        }

        if (maxLandArea != null) {
            spec = spec.and(FarmSpecifications.hasMaxLandArea(maxLandArea));
        }

        logger.debug("Searching farms with filters.");

        Page<Farm> farmPage = farmRepository.findAll(spec, pageable);

        SimplePageable simplifiedPageable = SimplePageable.
                builder().
                pageNumber(farmPage.getPageable().getPageNumber()).
                pageSize(farmPage.getPageable().getPageSize()).
                totalElements(farmPage.getTotalElements()).
                totalPages(farmPage.getTotalPages()).
                build();

        return new SimplePage<>(farmPage.getContent(), simplifiedPageable);
    }
}
