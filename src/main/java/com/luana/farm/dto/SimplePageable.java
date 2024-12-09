package com.luana.farm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SimplePageable {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
