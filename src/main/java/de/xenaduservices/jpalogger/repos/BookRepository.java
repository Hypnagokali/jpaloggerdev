package de.xenaduservices.jpalogger.repos;

import de.xenaduservices.jpalogger.data.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
}
