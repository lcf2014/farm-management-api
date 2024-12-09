package com.luana.farm.controller;

import com.luana.farm.dto.SimplePage;
import com.luana.farm.entities.Farm;
import com.luana.farm.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/farms")
@Validated
@RequiredArgsConstructor
public class FarmController {

    private final FarmService farmService;

    @Operation(summary = "Get a list of farms.",
            description = "Get a list of farms. The response is a list of Farms.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of farms",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Farm[].class))),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<SimplePage<Farm>> listFarms(
            @RequestParam(required = false) String cropType,
            @RequestParam(required = false) Double minLandArea,
            @RequestParam(required = false) Double maxLandArea,
            Pageable pageable
    ) {
        log.info("Listing farms with filters - cropType: {}, minLandArea: {}, maxLandArea: {}", cropType, minLandArea, maxLandArea);
        return ResponseEntity.ok(farmService.listFarms(cropType, minLandArea, maxLandArea, pageable));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created the resource",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Farm.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred",
                    content = @Content)
    })
    @Operation(summary = "Create a farm",
            description = "Create a farm. The response is created Farm object with id and other details.")
    @PostMapping
    public ResponseEntity<Farm> createFarm(@RequestBody @Valid Farm farm) {
        log.info("Creating a new farm: {}", farm.getName());
        return new ResponseEntity<>(farmService.createFarm(farm), HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted the resource",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred",
                    content = @Content)
    })
    @Operation(summary = "Delete a farm",
            description = "Delete a farm by id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarm(@PathVariable Long id) {
        log.info("Deleting farm with ID: {}", id);
        farmService.deleteFarm(id);
        return ResponseEntity.noContent().build();
    }
}
