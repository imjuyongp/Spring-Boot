package com.likelion.sbstudy.global.page.mapper;

import com.likelion.sbstudy.domain.book.dto.response.BookResponse;
import com.likelion.sbstudy.global.page.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

  public <T> PageResponse<T> toPageResponse(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .pageNum(page.getNumber() + 1)
        .pageSize(page.getSize())
        .last(page.isLast())
        .build();
  }

}

