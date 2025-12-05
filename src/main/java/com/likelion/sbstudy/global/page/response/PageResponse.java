package com.likelion.sbstudy.global.page.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "PageResponse DTO", description = "응답 객체들에 대한 리스트를 페이지로 응답 반환")
public class PageResponse<T> {

  @Schema(description = "데이터 리스트")
  private List<T> content;

  @Schema(description = "전체 데이터의 개수", example = "200")
  private Long totalElements;

  @Schema(description = "전체 페이지 개수", example = "50")
  private Integer totalPages;

  @Schema(description = "페이지 번호", example = "1")
  private Integer pageNum;

  @Schema(description = "페이지 크기", example = "4")
  private Integer pageSize;

  @Schema(description = "마지막 데이터 여부", example = "false")
  private Boolean last;
}

