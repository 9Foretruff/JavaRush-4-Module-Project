package com.javarush.domain.repository;

import com.javarush.domain.entity.City;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CityRepository extends RepositoryBase<Integer, City> {

    public CityRepository(EntityManager entityManager) {
        super(entityManager, City.class);
    }

    public long getTotalCount() {
        var query = getEntityManager().createQuery("select count(c) from City c", Long.class);
        return (long) query.getSingleResult();
    }

}