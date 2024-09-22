package de.xenaduservices.jpalogger.repos;

import java.util.Set;

import de.xenaduservices.jpalogger.data.Author;
import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Integer> {

    Set<Author> findAllByBooksIsbnContaining(String isbnLike);

    @Override
    @Nonnull
    Set<Author> findAll();

}
