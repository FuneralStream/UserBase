package org.example.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    @Getter
    private static SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
        throw new UnsupportedOperationException("Utility class!");
    }

    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistry standardRegistry = null;
        try {
            standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .getMetadataBuilder()
                    .build();

            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception ex) {
            if (standardRegistry != null) {
               StandardServiceRegistryBuilder.destroy(standardRegistry);
            }
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtil.sessionFactory = sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
