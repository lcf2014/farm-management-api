package com.luana.farm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Production {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CropType cropType;

    @NotNull
    private Boolean isIrrigated;

    @NotNull
    private Boolean isInsured;

    public enum CropType {
        RICE, BEANS, CORN, COFFEE, SOYBEANS
    }
}
