package com.mak.library.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(name = "uk_category_name", columnNames = "name"))
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 400)
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    public Category() {}
    public Category(String name, String description) {
        this.name = name; this.description = description;
    }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public Set<Book> getBooks(){ return books; }
    public void setName(String name){ this.name = name; }
    public void setDescription(String description){ this.description = description; }
}
