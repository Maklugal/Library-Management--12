package com.mak.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String name;

    private LocalDate birthDate;

    @Column(length = 120)
    private String country;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    public Author() {}

    public Author(String name, LocalDate birthDate, String country) {
        this.name = name;
        this.birthDate = birthDate;
        this.country = country;
    }

    // helper
    public void addBook(Book b){ books.add(b); b.setAuthor(this); }
    public void removeBook(Book b){ books.remove(b); b.setAuthor(null); }

    // getters / setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getCountry() { return country; }
    public List<Book> getBooks() { return books; }
    public void setName(String name) { this.name = name; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setCountry(String country) { this.country = country; }
}
