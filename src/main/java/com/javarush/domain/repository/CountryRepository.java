package com.javarush.domain.repository;

import com.javarush.domain.entity.Country;
import jakarta.persistence.EntityManager;

public class CountryRepository extends RepositoryBase<Integer, Country> {

    public CountryRepository(EntityManager entityManager) {
        super(entityManager, Country.class);
    }

}