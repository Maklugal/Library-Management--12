package com.mak.library;

import com.mak.library.model.*;
import com.mak.library.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        EntityManager em = JpaUtil.em();
        try {
            // 1) Idempotent seed: tekrar çalıştırılsa da duplicate üretmez
            insertSampleDataIdempotent(em);

            // 2) Özet yazdır
            printSummary(em);

            // 3) Yeni bir ödünç kaydı aç (returnDate=null)
            borrowNew(em);

            // 4) Açık (returnDate is null) en yeni kaydı iade et
            markReturnedToday(em);

        } finally {
            em.close();
            JpaUtil.close();
        }
    }

    // =========================================================
    // SEED (İDEMPOTENT)
    // =========================================================
    private static void insertSampleDataIdempotent(EntityManager em) {
        em.getTransaction().begin();

        // Author
        Author author = findOrCreateAuthor(em, "Isaac Asimov");
        author.setBirthDate(LocalDate.of(1920, 1, 2));
        author.setCountry("USA");

        // Publisher
        Publisher publisher = findOrCreatePublisher(em, "Spectra");
        publisher.setEstablishmentYear(1985);
        publisher.setAddress("New York, USA");

        // Categories
        Category cSciFi  = findOrCreateCategory(em, "Science Fiction", "Bilim kurgu kitapları");
        Category cClassic = findOrCreateCategory(em, "Classic", "Klasik eserler");

        // Book (isim + yazar ile tekillik varsayımı)
        Book book = findOrCreateBook(em, "Foundation", author, publisher, 1951, 5);

        // Many-to-Many bağlantıları varsa tekrar ekleme
        ensureCategoryLinked(book, cSciFi);
        ensureCategoryLinked(book, cClassic);

        em.getTransaction().commit();
    }

    // =========================================================
    // HELPERS (find-or-create ve ilişki kontrol)
    // =========================================================
    private static Author findOrCreateAuthor(EntityManager em, String name) {
        List<Author> list = em.createQuery(
                        "select a from Author a where lower(a.name)=lower(:n)", Author.class)
                .setParameter("n", name)
                .getResultList();
        if (!list.isEmpty()) return list.get(0);

        Author a = new Author(name, null, null);
        em.persist(a);
        return a;
    }

    private static Publisher findOrCreatePublisher(EntityManager em, String name) {
        List<Publisher> list = em.createQuery(
                        "select p from Publisher p where lower(p.name)=lower(:n)", Publisher.class)
                .setParameter("n", name)
                .getResultList();
        if (!list.isEmpty()) return list.get(0);

        Publisher p = new Publisher(name, null, null);
        em.persist(p);
        return p;
    }

    private static Category findOrCreateCategory(EntityManager em, String name, String desc) {
        List<Category> list = em.createQuery(
                        "select c from Category c where lower(c.name)=lower(:n)", Category.class)
                .setParameter("n", name)
                .getResultList();
        if (!list.isEmpty()) return list.get(0);

        Category c = new Category(name, desc);
        em.persist(c);
        return c;
    }

    private static Book findOrCreateBook(EntityManager em, String title, Author author,
                                         Publisher publisher, Integer year, Integer stock) {
        List<Book> list = em.createQuery(
                        "select b from Book b where lower(b.name)=lower(:n) and b.author=:a", Book.class)
                .setParameter("n", title)
                .setParameter("a", author)
                .getResultList();
        if (!list.isEmpty()) {
            Book existing = list.get(0);
            // var olan kitabın eksik alanlarını güncelle (opsiyonel)
            if (existing.getPublisher() == null) existing.setPublisher(publisher);
            if (existing.getPublicationYear() == null && year != null) existing.setPublicationYear(year);
            if (existing.getStock() == null && stock != null) existing.setStock(stock);
            return existing;
        }

        Book b = new Book(title, year, stock);
        b.setAuthor(author);
        b.setPublisher(publisher);
        em.persist(b);
        return b;
    }

    private static void ensureCategoryLinked(Book book, Category cat) {
        if (!book.getCategories().contains(cat)) {
            book.addCategory(cat); // iki yönlü ilişkiyi helper kuruyor
        }
    }

    // =========================================================
    // DEMO İŞLEMLERİ
    // =========================================================
    private static void printSummary(EntityManager em) {
        em.getTransaction().begin();
        var list = em.createQuery("select b from Book b", Book.class).getResultList();
        for (Book b : list) {
            System.out.printf("Kitap: %s | Yazar: %s | Yayinevi: %s | Kategoriler: %d | Borrowings: %d%n",
                    b.getName(),
                    b.getAuthor().getName(),
                    b.getPublisher().getName(),
                    b.getCategories().size(),
                    b.getBorrowings().size());
        }
        em.getTransaction().commit();
    }

    private static void borrowNew(EntityManager em) {
        em.getTransaction().begin();
        Book b = em.createQuery("select b from Book b where lower(b.name)=:n", Book.class)
                .setParameter("n", "foundation")
                .setMaxResults(1)
                .getSingleResult();

        BookBorrowing bb = new BookBorrowing("Test Kullanıcı", LocalDate.now()); // returnDate = null
        b.addBorrowing(bb); // cascade ile persist olur

        em.getTransaction().commit();
        System.out.println("Yeni ödünç kaydı açıldı: " + b.getName());
    }

    private static void markReturnedToday(EntityManager em) {
        em.getTransaction().begin();
        List<BookBorrowing> list = em.createQuery(
                        "select bb from BookBorrowing bb where bb.returnDate is null order by bb.id desc",
                        BookBorrowing.class)
                .setMaxResults(1)
                .getResultList();

        if (!list.isEmpty()) {
            BookBorrowing bb = list.get(0);
            bb.setReturnDate(LocalDate.now());
            System.out.println("Teslim tarihi güncellendi: " + bb.getReturnDate());
        } else {
            System.out.println("Güncellenecek aktif (returnDate is null) kayıt yok.");
        }
        em.getTransaction().commit();
    }
}
