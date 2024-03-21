package com.javarush.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.dao.CityDAO;
import com.javarush.domain.dao.CountryDAO;
import com.javarush.domain.redis.util.RedisUtil;
import com.javarush.domain.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class Main {
    private final SessionFactory sessionFactory;
    private final RedisUtil redisUtil;

    private final ObjectMapper mapper;

    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;

    public static void main(String[] args) {
        sessionFactory = HibernateUtil.getSessionFactory();
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);

        redisUtil = prepareRedisClient();
        mapper = new ObjectMapper();
    }

}
