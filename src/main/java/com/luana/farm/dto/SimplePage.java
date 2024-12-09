package com.luana.farm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class SimplePage<T> {
    private List<T> content;
    private SimplePageable pageable;
}
