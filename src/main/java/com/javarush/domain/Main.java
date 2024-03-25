package com.javarush.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.entity.City;
import com.javarush.domain.menu.Menu;
import com.javarush.domain.redis.util.RedisUtil;
import com.javarush.domain.repository.CityRepository;
import com.javarush.domain.repository.CountryRepository;
import com.javarush.domain.util.HibernateUtil;
import org.hibernate.Session;

import java.lang.reflect.Proxy;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var sessionFactory = HibernateUtil.getSessionFactory();
        var session = (Session) Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1)
        );

        var cityRepository = new CityRepository(session);
        var countryRepository = new CountryRepository(session);

        var redisClient = RedisUtil.getJedis();
        var mapper = new ObjectMapper();

        Menu menu = new Menu(sessionFactory, redisClient, mapper, cityRepository, countryRepository);
        List<City> allCities = menu.fetchData();
        menu.shutdown();
    }

}