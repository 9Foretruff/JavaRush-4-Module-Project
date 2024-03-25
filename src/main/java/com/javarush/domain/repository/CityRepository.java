package com.javarush.domain.repository;

import com.javarush.domain.entity.City;
import jakarta.persistence.EntityManager;

public class CityRepository extends RepositoryBase<Integer, City> {

    public CityRepository(EntityManager entityManager) {
        super(entityManager, City.class);
    }

    public long getTotalCount() {
        var query = getEntityManager().createQuery("select count(c) from City c", Long.class);
        return query.getSingleResult();
    }

}