package de.xenaduservices.jpalogger.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;

import de.xenaduservices.jpalogger.repos.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.type.internal.ParameterizedTypeImpl;

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
            String entityName = getTestEntityName();

            if ( args != null && args.length > 0 && entityName != null ) {
                String sql = (String) args[0];
                if (sql.contains( " " + entityName + " ")) {
                log.info( "{}", args[0] );
                }
            }
        }

        return method.invoke( connection, args );
    }

    private static String getTestEntityName() throws NoSuchMethodException {
        String entityName = null;
        Method testMethod = AuthorRepository.class.getMethod( "findAllByBooksIsbnContaining", String.class );

        Type[] actualTypeArguments = ( (ParameterizedType) testMethod.getGenericReturnType() ).getActualTypeArguments();

        if (actualTypeArguments.length > 0) {
            String typeName = actualTypeArguments[0].getTypeName();
            int i = typeName.lastIndexOf( '.' );
            entityName = typeName.substring( i + 1 ).toLowerCase();
        }
        return entityName;
    }
}
