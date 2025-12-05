package com.likelion.sbstudy.domain.book.service;
import com.likelion.sbstudy.domain.book.dto.request.CreateBookRequest;
import com.likelion.sbstudy.domain.book.dto.response.BookResponse;
import com.likelion.sbstudy.domain.book.entity.Book;
import com.likelion.sbstudy.domain.book.entity.BookImage;
import com.likelion.sbstudy.domain.book.entity.Category;
import com.likelion.sbstudy.domain.book.exception.BookErrorCode;
import com.likelion.sbstudy.domain.book.mapper.BookMapper;
import com.likelion.sbstudy.domain.book.repository.BookRepository;
import com.likelion.sbstudy.global.exception.CustomException;
import com.likelion.sbstudy.global.page.response.InfiniteResponse;
import com.likelion.sbstudy.global.page.response.PageResponse;
import com.likelion.sbstudy.global.s3.entity.PathName;
import com.likelion.sbstudy.global.s3.service.S3Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {

  public BookResponse createBook(CreateBookRequest request, List<MultipartFile> images);

  /**
   * 특정 카테고리에 속한 도서를 페이지 단위로 조회합니다.
   *
   * @param category 조회할 도서 카테고리
   * @param pageable 페이징 및 정렬 정보
   * @return 카테고리별 도서 목록 페이지
   */
  PageResponse<BookResponse> getBookPageByCategory(Category category, Pageable pageable);

  /**
   * 특정 카테고리에 속한 도서를 인피니티 스크롤 방식으로 조회합니다.
   *
   * @param category 조회할 도서 카테고리
   * @param lastBookId 이전 조회에서 마지막으로 가져온 도서 ID (처음 조회 시 null)
   * @param size 한 번에 가져올 도서 개수
   * @return 조회된 도서 목록
   */
  InfiniteResponse<BookResponse> getBooksByCategoryInfinite(
      Category category, Long lastBookId, Integer size);


}
