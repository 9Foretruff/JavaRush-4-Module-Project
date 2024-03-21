package com.javarush.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.dao.CityDAO;
import com.javarush.domain.dao.CountryDAO;
import com.javarush.domain.entity.City;
import com.javarush.domain.redis.util.RedisUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class Menu {
    private final SessionFactory sessionFactory;
    private final Jedis redisClient;

    private final ObjectMapper mapper;

    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;

    public Menu(SessionFactory sessionFactory, Jedis redisClient, ObjectMapper mapper, CityDAO cityDAO, CountryDAO countryDAO) {
        this.sessionFactory = sessionFactory;
        this.redisClient = redisClient;
        this.mapper = mapper;
        this.cityDAO = cityDAO;
        this.countryDAO = countryDAO;
    }

    private List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    private void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }
}
