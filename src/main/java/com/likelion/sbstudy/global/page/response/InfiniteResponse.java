package com.likelion.sbstudy.global.page.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "InfiniteResponse DTO", description = "응답 객체들에 대한 리스트를 무한 스크롤로 응답 반환")
public class InfiniteResponse<T> {

  @Schema(description = "데이터 리스트")
  private List<T> content;

  @Schema(description = "마지막 데이터의 커서 값")
  private Long lastCursor;

  @Schema(description = "더 가져올 데이터가 있는지 여부")
  private Boolean hasNext;

  @Schema(description = "한 번에 가져온 데이터 개수", example = "20")
  private Integer size;
}

