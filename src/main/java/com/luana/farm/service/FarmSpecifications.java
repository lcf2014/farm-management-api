package com.luana.farm.service;

import com.luana.farm.entities.Farm;
import org.springframework.data.jpa.domain.Specification;

public class FarmSpecifications {
    public static Specification<Farm> hasCropType(final String cropType) {
        return (root, query, cb) -> cb.equal(root.join("productions").get("cropType"), cropType);
    }

    public static Specification<Farm> hasMinLandArea(final Double minLandArea) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("landArea"), minLandArea);
    }

    public static Specification<Farm> hasMaxLandArea(final Double maxLandArea) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("landArea"), maxLandArea);
    }
}
