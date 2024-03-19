package com.javarush.domain;

import com.javarush.domain.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.SessionFactory;

public class Main {
//    private final SessionFactory sessionFactory;
//    private final RedisClient redisClient;
//
//    private final ObjectMapper mapper;
//
//    private final CityDAO cityDAO;
//    private final CountryDAO countryDAO;

    public static void main(String[] args) {
        @Cleanup var sessionFactory = HibernateUtil.getSessionFactory();

    }

}
