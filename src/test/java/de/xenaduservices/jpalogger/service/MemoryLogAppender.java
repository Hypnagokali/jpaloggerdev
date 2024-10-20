package de.xenaduservices.jpalogger.service;

import java.util.List;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class MemoryLogAppender extends ListAppender<ILoggingEvent> {

    public List<String> logContainsAll(String... strings) {
        return this.list.stream()
            .filter( ev -> doContain(ev, strings) )
            .map( ILoggingEvent::toString )
            .toList();
    }

    private boolean doContain(ILoggingEvent ev, String[] strings) {
        for ( String string : strings ) {
            if (!ev.toString().contains(string)) return false;
        }

        return true;
    }

}
