package de.xenaduservices.jpalogger.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.xenaduservices.jpalogger.annotations.LogQueries;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String isbn;

    @ManyToMany(mappedBy = "books")
    public Set<Author> authors = new HashSet<>();

    public Book(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Book book = (Book) o;

        return Objects.equals( isbn, book.isbn );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( isbn );
    }
}
