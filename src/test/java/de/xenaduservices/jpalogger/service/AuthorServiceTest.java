package de.xenaduservices.jpalogger.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import de.xenaduservices.jpalogger.data.Author;
import de.xenaduservices.jpalogger.data.Book;
import de.xenaduservices.jpalogger.logging.ConnectionLoggingInvocationHandler;
import de.xenaduservices.jpalogger.repos.AuthorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;

    MemoryLogAppender memoryLogAppender;

    @BeforeEach
    void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger( ConnectionLoggingInvocationHandler.class );
        memoryLogAppender = new MemoryLogAppender();
        memoryLogAppender.setContext( (LoggerContext) LoggerFactory.getILoggerFactory() );
        logger.setLevel( Level.DEBUG );
        logger.addAppender(memoryLogAppender);
        memoryLogAppender.start();

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
        Set<Book> allBook = authorService.getAuthorsWhoHasWrittenABookContainingIsbn( "45" )
            .stream()
            .flatMap( author -> author.getBooks().stream() )
            .collect( Collectors.toSet() );

        assertThat( allBook )
            .hasSize( 2 )
            .extracting( Book::getTitle )
            .containsExactly( "The Talisman", "The long walk" );
    }

    @Test
    void entityBasedLogging() {
        // e.g.: annotate an Entity with @LogQueries
        List<String> authorLogs = memoryLogAppender.logContainsAll( "select", "from author" );
        List<String> bookLogs = memoryLogAppender.logContainsAll( "select", "from book" );


        assertThat( authorLogs ).isNotEmpty();
        assertThat( bookLogs ).isEmpty();
    }

    @Test
    void methodBasedLogging() {
        // e.g.: annotate a repository-method with @LogQueries

        assertThat( true ).isFalse();
    }
}