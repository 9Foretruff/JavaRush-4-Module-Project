package com.javarush.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.entity.City;
import com.javarush.domain.mapper.CityCountryMapper;
import com.javarush.domain.redis.entity.CityCountry;
import com.javarush.domain.redis.util.RedisUtil;
import com.javarush.domain.repository.CityRepository;
import com.javarush.domain.repository.CountryRepository;
import com.javarush.domain.util.HibernateUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataPreparationService {

    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final ObjectMapper mapper;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public DataPreparationService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.redisClient = RedisUtil.getRedisClient();
        this.mapper = new ObjectMapper();
        this.cityRepository = new CityRepository(sessionFactory.getCurrentSession());
        this.countryRepository = new CountryRepository(sessionFactory.getCurrentSession());
    }

    public void init() {
        List<City> allCities = fetchDataWithPagination(500);
        List<CityCountry> preparedData = CityCountryMapper.transformData(allCities);
        pushToRedis(preparedData);
    }

    private List<City> fetchDataWithPagination(int pageSize) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            long totalCount = cityRepository.getTotalCount();
            int currentPage = 0;
            List<City> allCities = new ArrayList<>();

            while ((long) currentPage * pageSize < totalCount) {
                List<City> cities = cityRepository.findAll(currentPage * pageSize, pageSize);
                allCities.addAll(cities);
                currentPage++;
            }

            session.getTransaction().commit();
            return allCities;
        }
    }

    private void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException exception) {
                    log.error("Exception while pushing to redis", exception);
                }
            }

        }
    }
}
