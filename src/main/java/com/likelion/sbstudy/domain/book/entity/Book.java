package com.likelion.sbstudy.domain.book.entity;

import com.likelion.sbstudy.global.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "book")
public class Book extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "author", nullable = false)
  private String author;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "release_date", nullable = false)
  private String releaseDate;

  @ElementCollection(fetch = FetchType.LAZY)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"))
  @Column(name = "category")
  @Builder.Default
  private List<Category> categoryList = new ArrayList<>();

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BookImage> bookImageList;

  public void addBookImageList(List<BookImage> bookImageList) {
    this.bookImageList = bookImageList;
  }

  public void addCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  public void update(Book book) {
    this.title = book.getTitle();
    this.author = book.getAuthor();
    this.publisher = book.getPublisher();
    this.price = book.getPrice();
    this.description = book.getDescription();
    this.releaseDate = book.getReleaseDate();
  }
}
