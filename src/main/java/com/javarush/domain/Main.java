package com.javarush.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.domain.dao.CityDAO;
import com.javarush.domain.dao.CountryDAO;
import com.javarush.domain.redis.util.RedisUtil;
import com.javarush.domain.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class Main {

    public static void main(String[] args) {
        var sessionFactory = HibernateUtil.getSessionFactory();
        var cityDAO = new CityDAO(sessionFactory);
        var countryDAO = new CountryDAO(sessionFactory);

        var redisUtil = RedisUtil.getJedis();
        var mapper = new ObjectMapper();
        Menu menu = new Menu(sessionFactory, redisUtil, mapper, cityDAO, countryDAO);
    }

}
