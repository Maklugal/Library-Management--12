package com.mak.library.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String name;

    private Integer establishmentYear;

    @Column(length = 255)
    private String address;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    public Publisher() {}

    public Publisher(String name, Integer establishmentYear, String address) {
        this.name = name;
        this.establishmentYear = establishmentYear;
        this.address = address;
    }

    // helper
    public void addBook(Book b){ books.add(b); b.setPublisher(this); }
    public void removeBook(Book b){ books.remove(b); b.setPublisher(null); }

    // getters / setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getEstablishmentYear() { return establishmentYear; }
    public String getAddress() { return address; }
    public List<Book> getBooks() { return books; }
    public void setName(String name) { this.name = name; }
    public void setEstablishmentYear(Integer establishmentYear) { this.establishmentYear = establishmentYear; }
    public void setAddress(String address) { this.address = address; }
}
