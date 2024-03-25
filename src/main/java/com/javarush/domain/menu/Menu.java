package com.javarush.domain.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.entity.City;
import com.javarush.domain.repository.CityRepository;
import com.javarush.domain.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class Menu {
    private final SessionFactory sessionFactory;
//    private final Jedis jedis;
    private final ObjectMapper mapper;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

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

    public void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
//        if (nonNull(jedis)) {
//            jedis.shutdown();
//        }
    }

}