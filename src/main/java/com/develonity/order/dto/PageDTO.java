package com.develonity.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {

    private int currentPage;
    private int size;
    private String sortBy;
    private boolean ascOrDesc;

    public Pageable toPageable() {
        Sort.Direction direction = ascOrDesc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return PageRequest.of(currentPage - 1, size, sort);

    }
}
