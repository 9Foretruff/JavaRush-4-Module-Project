package com.javarush.domain.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.entity.City;
import com.javarush.domain.entity.Country;
import com.javarush.domain.entity.CountryLanguage;
import com.javarush.domain.exception.PushToRedisException;
import com.javarush.domain.redis.entity.CityCountry;
import com.javarush.domain.redis.entity.Language;
import com.javarush.domain.redis.util.RedisUtil;
import com.javarush.domain.repository.CityRepository;
import com.javarush.domain.repository.CountryRepository;
import com.javarush.domain.util.HibernateUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Menu {
    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final ObjectMapper mapper;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public Menu() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.redisClient = RedisUtil.getRedisClient();
        this.mapper = new ObjectMapper();
        this.cityRepository = new CityRepository(sessionFactory.getCurrentSession());
        this.countryRepository = new CountryRepository(sessionFactory.getCurrentSession());
    }

    public void run() {
        List<City> allCities = fetchData();
        List<CityCountry> preparedData = transformData(allCities);
        pushToRedis(preparedData);

        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }

    public List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();

            long totalCount = cityRepository.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityRepository.findAll(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    public List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(this::transformCity).collect(Collectors.toList());
    }

    private CityCountry transformCity(City city) {
        CityCountry res = new CityCountry();
        res.setId(city.getId());
        res.setName(city.getName());
        res.setPopulation(city.getPopulation());
        res.setDistrict(city.getDistrict());

        Country country = city.getCountry();
        res.setAlternativeCountryCode(country.getAlternativeCode());
        res.setContinent(country.getContinent());
        res.setCountryCode(country.getCode());
        res.setCountryName(country.getName());
        res.setCountryPopulation(country.getPopulation());
        res.setCountryRegion(country.getRegion());
        res.setCountrySurfaceArea(country.getSurfaceArea());
        Set<CountryLanguage> countryLanguages = country.getLanguages();
        res.setLanguages(transformLanguages(countryLanguages));

        return res;
    }

    private Set<Language> transformLanguages(Set<CountryLanguage> countryLanguages) {
        return countryLanguages.stream().map(this::transformLanguage).collect(Collectors.toSet());
    }

    private Language transformLanguage(CountryLanguage cl) {
        Language language = new Language();
        language.setLanguage(cl.getLanguage());
        language.setIsOfficial(cl.getIsOfficial());
        language.setPercentage(cl.getPercentage());
        return language;
    }

    public void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException exception) {
                    throw new PushToRedisException("Exception while pushing to redis");
                }
            }

        }
    }

}