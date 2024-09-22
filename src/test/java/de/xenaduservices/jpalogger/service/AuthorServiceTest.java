package de.xenaduservices.jpalogger.service;

import java.util.Set;
import java.util.stream.Collectors;

import de.xenaduservices.jpalogger.data.Author;
import de.xenaduservices.jpalogger.data.Book;
import de.xenaduservices.jpalogger.repos.AuthorRepository;
import de.xenaduservices.jpalogger.repos.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        Author king = authorRepository.save( new Author( "Stephen", "King" ) );
        Author straub = authorRepository.save( new Author( "Peter", "Straub" ) );

        Book theTalisman = new Book( "The Talisman", "9780345444882" );
        Book theLongWork = new Book( "The long walk", "9780450049576" );
        king.addBook( theTalisman );
        king.addBook( theLongWork );
        straub.addBook( theTalisman );

        authorRepository.save( king );
        authorRepository.save( straub );
    }

    @Test
    @Transactional
    void testBasicFunctionality() {
        Set<Book> allBook = authorService.getAuthorsWhoHasWrittenABookContainingIsbn("45")
            .stream()
            .flatMap( author -> author.getBooks().stream() )
            .collect( Collectors.toSet() );

        assertThat( allBook )
            .hasSize( 2 )
            .extracting( Book::getTitle )
            .containsExactly( "The Talisman", "The long walk" );
    }
}