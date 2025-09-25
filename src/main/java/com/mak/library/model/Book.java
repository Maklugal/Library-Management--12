package com.mak.library.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "books")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 220)
    private String name;

    private Integer publicationYear;

    @Column(nullable = false)
    private Integer stock = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_book_author"))
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false, foreignKey = @ForeignKey(name = "fk_book_publisher"))
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_bc_book")),
            inverseJoinColumns = @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_bc_category")),
            uniqueConstraints = @UniqueConstraint(name = "uk_book_category", columnNames = {"book_id","category_id"}))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookBorrowing> borrowings = new ArrayList<>();

    public Book() {}
    public Book(String name, Integer publicationYear, Integer stock) {
        this.name = name; this.publicationYear = publicationYear; this.stock = stock;
    }

    public Book(String foundation, int i, int i1, Author author, Publisher publisher) {
    }

    public void addCategory(Category c){ categories.add(c); c.getBooks().add(this); }
    public void removeCategory(Category c){ categories.remove(c); c.getBooks().remove(this); }

    public void addBorrowing(BookBorrowing b){ borrowings.add(b); b.setBook(this); }
    public void removeBorrowing(BookBorrowing b){ borrowings.remove(b); b.setBook(null); }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public Integer getPublicationYear(){ return publicationYear; }
    public Integer getStock(){ return stock; }
    public Author getAuthor(){ return author; }
    public Publisher getPublisher(){ return publisher; }
    public Set<Category> getCategories(){ return categories; }
    public List<BookBorrowing> getBorrowings(){ return borrowings; }
    public void setName(String name){ this.name = name; }
    public void setPublicationYear(Integer publicationYear){ this.publicationYear = publicationYear; }
    public void setStock(Integer stock){ this.stock = stock; }
    public void setAuthor(Author author){ this.author = author; }
    public void setPublisher(Publisher publisher){ this.publisher = publisher; }
}
