package com.likelion.sbstudy.global.page.mapper;

import java.util.List;

import com.likelion.sbstudy.global.page.response.InfiniteResponse;
import org.springframework.stereotype.Component;

@Component
public class InfiniteMapper {

  public <T> InfiniteResponse<T> toInfiniteResponse(
      List<T> content, Long lastCursor, boolean hasNext, int size) {
    return InfiniteResponse.<T>builder()
        .content(content)
        .lastCursor(lastCursor)
        .hasNext(hasNext)
        .size(size)
        .build();
  }
}

