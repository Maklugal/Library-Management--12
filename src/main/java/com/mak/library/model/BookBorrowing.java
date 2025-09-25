package com.mak.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book_borrowings")
public class BookBorrowing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String borrowerName;

    @Column(nullable = false)
    private LocalDate borrowingDate;

    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false, foreignKey = @ForeignKey(name = "fk_borrowing_book"))
    private Book book;

    public BookBorrowing() {}
    public BookBorrowing(String borrowerName, LocalDate borrowingDate) {
        this.borrowerName = borrowerName; this.borrowingDate = borrowingDate;
    }

    public Long getId(){ return id; }
    public String getBorrowerName(){ return borrowerName; }
    public LocalDate getBorrowingDate(){ return borrowingDate; }
    public LocalDate getReturnDate(){ return returnDate; }
    public Book getBook(){ return book; }
    public void setBorrowerName(String borrowerName){ this.borrowerName = borrowerName; }
    public void setBorrowingDate(LocalDate borrowingDate){ this.borrowingDate = borrowingDate; }
    public void setReturnDate(LocalDate returnDate){ this.returnDate = returnDate; }
    public void setBook(Book book){ this.book = book; }
}
