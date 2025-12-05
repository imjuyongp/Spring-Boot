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
import com.likelion.sbstudy.global.page.mapper.InfiniteMapper;
import com.likelion.sbstudy.global.page.mapper.PageMapper;
import com.likelion.sbstudy.global.page.response.InfiniteResponse;
import com.likelion.sbstudy.global.page.response.PageResponse;
import com.likelion.sbstudy.global.s3.entity.PathName;
import com.likelion.sbstudy.global.s3.service.S3Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final S3Service s3Service;
  private final BookMapper bookMapper;
  private final InfiniteMapper infiniteMapper;
  private final PageMapper pageMapper;

  @Override
  @Transactional
  public BookResponse createBook(CreateBookRequest request, List<MultipartFile> images) {

    if (bookRepository.findByTitleAndAuthor(request.getTitle(), request.getAuthor()).isPresent()) {
      throw new CustomException(BookErrorCode.BOOK_ALREADY_EXISTS);
    }

    Book book = Book.builder()
        .title(request.getTitle())
        .author(request.getAuthor())
        .publisher(request.getPublisher())
        .price(request.getPrice())
        .description(request.getDescription())
        .releaseDate(request.getReleaseDate())
        .build();

    // categoryList 설정
    book.addCategoryList(request.getCategoryList());

    // 먼저 Book을 저장해야 ID가 생성됨
    bookRepository.save(book);

    // images가 null이 아닐 때만 이미지 처리
    List<BookImage> bookImages = new ArrayList<>();
    if (images != null && !images.isEmpty()) {
      bookImages = images.stream()
          .filter(image -> !image.isEmpty())
          .map(image -> {
            String imageUrl = s3Service.uploadFile(PathName.FOLDER1, image);
            return BookImage.builder()
                .imageUrl(imageUrl)
                .book(book)
                .build();
          })
          .toList();
    }

    book.addBookImageList(bookImages);

    bookRepository.save(book);

    return bookMapper.toBookResponse(book);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<BookResponse> getBookPageByCategory(Category category, Pageable pageable) {

    Page<BookResponse> bookPage =
        bookRepository
            .findAllByCategoryListContaining(category, pageable)
            .map(bookMapper::toBookResponse);

    log.info(
        "책 페이지 조회 성공: category={}, pageNumber={}, totalElements={}",
        category,
        pageable.getPageNumber(),
        bookPage.getTotalElements());
    return pageMapper.toPageResponse(bookPage);
  }

  @Override
  @Transactional(readOnly = true)
  public InfiniteResponse<BookResponse> getBooksByCategoryInfinite(
      Category category, Long lastBookId, Integer size) {

    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.DESC, "id"));
    List<Book> books;

    if (lastBookId == null) {
      books = bookRepository.findAllByCategoryListContaining(category, pageable).getContent();
    } else {
      books =
          bookRepository
              .findAllByCategoryListContainingAndIdLessThan(category, lastBookId, pageable)
              .getContent();
    }

    boolean hasNext = books.size() > size;
    if (hasNext) {
      books = books.subList(0, size);
    }

    List<BookResponse> bookResponseList = books.stream().map(bookMapper::toBookResponse).toList();

    Long newLastCursor = books.isEmpty() ? null : books.getLast().getId();

    log.info("책 인피니티 스크롤 조회 성공: category={}, lastBookId={}, size={}", category, lastBookId, size);
    return infiniteMapper.toInfiniteResponse(bookResponseList, newLastCursor, hasNext, size);
  }

}
