package de.xenaduservices.jpalogger.annotations;

import java.util.ArrayList;
import java.util.List;

import de.xenaduservices.jpalogger.logging.EntityLoggingInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AnnotationUtil {

    public static List<EntityLoggingInfo> findAllEntityLoggingInfos() {
        // ToDo: should be cached !!
        List<EntityLoggingInfo> entityLoggingInfos = new ArrayList<>();

        try (ScanResult result = new ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .scan()) {

            for ( ClassInfo info : result.getClassesWithAnnotation( LogQueries.class ) ) {
                entityLoggingInfos.add( new EntityLoggingInfo( info.getSimpleName().toLowerCase() ) );
            }

        }

        return entityLoggingInfos;
    }

}
