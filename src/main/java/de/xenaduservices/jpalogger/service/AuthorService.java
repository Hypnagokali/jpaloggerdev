package de.xenaduservices.jpalogger.service;

import java.util.Set;

import de.xenaduservices.jpalogger.data.Author;
import de.xenaduservices.jpalogger.repos.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Set<Author> getAuthorsWhoHasWrittenABookContainingIsbn(String isbnLike) {
        return authorRepository.findAllByBooksIsbnContaining( isbnLike );
    }


}
