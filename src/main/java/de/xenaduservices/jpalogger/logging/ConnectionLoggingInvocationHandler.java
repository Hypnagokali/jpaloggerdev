package de.xenaduservices.jpalogger.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import de.xenaduservices.jpalogger.annotations.AnnotationUtil;
import de.xenaduservices.jpalogger.repos.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ConnectionLoggingInvocationHandler implements InvocationHandler {

    private final Connection connection;

    public static Connection wrap(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
            connection.getClass().getClassLoader(),
            new Class[] { Connection.class },
            new ConnectionLoggingInvocationHandler( connection )
        );
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if ( "prepareStatement".equals( method.getName() ) ) {
            List<String> entityNames = getEntityNamesByAnnotation();

            if ( args != null && args.length > 0 && !entityNames.isEmpty() ) {

                for ( String entityName : entityNames ) {
                    String sql = (String) args[0];
                    if (sql.contains( " " + entityName + " ")) {
                        log.info( "{}", args[0] );
                    }
                }

            }
        }

        return method.invoke( connection, args );
    }

    private static List<String> getEntityNamesByAnnotation() throws NoSuchMethodException {
        return AnnotationUtil.findAllEntityLoggingInfos().stream()
            .map( EntityLoggingInfo::getEntityName )
            .toList();
    }
}
