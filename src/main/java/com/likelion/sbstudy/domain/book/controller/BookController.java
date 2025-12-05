package com.likelion.sbstudy.domain.book.controller;

import com.likelion.sbstudy.domain.book.dto.request.CreateBookRequest;
import com.likelion.sbstudy.domain.book.dto.request.SaveBookRequest;
import com.likelion.sbstudy.domain.book.dto.request.UpdateBookRequest;
import com.likelion.sbstudy.domain.book.dto.response.BookResponse;
import com.likelion.sbstudy.domain.book.entity.Category;
import com.likelion.sbstudy.domain.book.service.BookService;
import com.likelion.sbstudy.global.exception.CustomException;
import com.likelion.sbstudy.global.page.exeption.PageErrorStatus;
import com.likelion.sbstudy.global.page.response.InfiniteResponse;
import com.likelion.sbstudy.global.page.response.PageResponse;
import com.likelion.sbstudy.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Post", description = "북마켓 API")
public class BookController {

  private final BookService bookService;

  @Operation(
      summary = "새 책 등록",
      description = "새로운 책을 등록하고, 등록된 책 정보를 반환합니다. (201 Created)")
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<BookResponse>> createBook(
      @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
      @RequestPart(value = "book") @Valid CreateBookRequest request,
      @Parameter(description = "책 이미지들",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
      @RequestPart(value = "images", required = false)
      List<MultipartFile> images) {
    BookResponse response = bookService.createBook(request, images);
    return ResponseEntity.ok(BaseResponse.success("책 생성에 성공하였습니다.", response));
  }

  @Operation(summary = "책 페이지 조회", description = "정렬 기준에 맞춰 책 페이지 정보를 조회합니다.")
  @GetMapping("/page")
  public ResponseEntity<BaseResponse<PageResponse<BookResponse>>> getBookPageByCategory(
      @Parameter(description = "정렬 기준", example = "NOVEL") @RequestParam Category category,
      @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1")
      Integer pageNum,
      @Parameter(description = "페이지 크기", example = "3") @RequestParam(defaultValue = "4")
      Integer pageSize) {
    org.springframework.data.domain.Pageable pageable = validatePageable(pageNum, pageSize);
    PageResponse<BookResponse> pageResponse = bookService.getBookPageByCategory(category, pageable);

    return ResponseEntity.ok(BaseResponse.success("페이지 조회에 성공했습니다.", pageResponse));
  }

  @Operation(summary = "책 인피니티 스크롤 조회", description = "마지막으로 조회한 책 식별자 이후의 책 목록을 조회합니다.")
  @GetMapping("/infinite")
  public ResponseEntity<BaseResponse<InfiniteResponse<BookResponse>>> getBooksByCategoryInfinite(
      @Parameter(description = "조회 기준 카테고리", example = "NOVEL") @RequestParam Category category,
      @Parameter(description = "마지막으로 조회한 책 식별자(첫 조회 시 생략)", example = "3")
      @RequestParam(required = false)
      Long lastBookId,
      @Parameter(description = "한 번에 조회할 책 개수", example = "3") @RequestParam(defaultValue = "3")
      Integer size) {
    InfiniteResponse<BookResponse> response =
        bookService.getBooksByCategoryInfinite(category, lastBookId, size);

    return ResponseEntity.ok(BaseResponse.success("인피니티 스크롤 조회에 성공했습니다.", response));
  }

  private org.springframework.data.domain.Pageable validatePageable(Integer pageNum, Integer pageSize) {
    if (pageNum < 1) {
      throw new CustomException(PageErrorStatus.PAGE_NOT_FOUND);
    }
    if (pageSize < 1) {
      throw new CustomException(PageErrorStatus.PAGE_SIZE_ERROR);
    }

    return PageRequest.of(pageNum - 1, pageSize);
  }

  /*@Operation(summary = "책 정보 수정",
      description = "첵 목록 페이지에서 책 정보 수정 후 수정 완료 버튼을 눌러쓸 때 요청되는 API")
  @PutMapping("/books/{id}")
  public ResponseEntity<UpdateBookResponse> updateBook(@RequestBody UpdateBookRequest updateBookRequest,
      @Parameter(description = "책 일련번호") @PathVariable Long id) { // dto
    return ResponseEntity.ok(bookService.updateBook(id, updateBookRequest));
  }

  @Operation(summary = "장바구니 넣기",
      description = "책 목록 페이지에서 장바구니 넣기 버튼을 눌렀을 때 요청되는 API")
  @PostMapping("/books/basket/{id}")
  public ResponseEntity<SaveBookResponse> saveBook(@RequestBody SaveBookRequest saveBookRequest,
      @Parameter(description = "책 일련번호") @PathVariable Long id) {
    return ResponseEntity.ok(bookService.saveBook(id, saveBookRequest));
  }*/

  /*
  @Operation(summary = "책 주문하기",
      description = "책 목록 페이지에서 주문하기 버튼을 눌렀을 때 요청되는 API")
  @PostMapping("/books")
  public String orderBook(@Parameter(description = "책 정보") @RequestBody CreateBookRequest createBookRequest) { // dto
    return "책 주문";
  }
   */

  /*@Operation(summary = "책 전체 조회",
      description = "책 목록 페이지로 이동할 때 요청되는 API")
  @GetMapping("/books")
  public ResponseEntity<List<BookResponse>> getAllBooks() {
    return ResponseEntity.ok(bookService.getAllBooks());
  }

  @Operation(summary = "책 단일 조회",
      description = "책 목록 페이지에서 특정 책에 접근할 때 요청되는 API")
  @GetMapping("/books/{id}")
  public ResponseEntity<BookResponse> getBookById(@Parameter(description = "특정 게시글 ID") @PathVariable Long id) {
    return ResponseEntity.ok(bookService.getBook(id));
  }

  @Operation(summary = "책 삭제",
      description = "책 목록 페이지에서 책 삭제 버튼을 눌렀을 때 요청되는 API")
  @DeleteMapping("/books/{id}")
  public ResponseEntity<Boolean> deleteBook(@Parameter(description = "책 일련번호") @PathVariable Long id) {
    return ResponseEntity.ok(bookService.deleteBook(id));
  }*/

}
