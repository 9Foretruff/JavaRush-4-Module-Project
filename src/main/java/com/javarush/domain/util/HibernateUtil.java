package com.javarush.domain.util;

import com.javarush.domain.exception.SessionFactoryInitializationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
@Slf4j
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = buildConfiguration().configure().buildSessionFactory();
                log.info("SessionFactory initialized successfully.");
            } catch (Exception e) {
                log.error("Failed to initialize SessionFactory", e);
                throw new SessionFactoryInitializationException("Failed to initialize SessionFactory");
            }
        }
        return sessionFactory;
    }

    public static Configuration buildConfiguration() {
        return new Configuration();
    }

}