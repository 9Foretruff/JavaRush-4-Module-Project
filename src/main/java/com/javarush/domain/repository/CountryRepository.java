package com.javarush.domain.repository;

import com.javarush.domain.entity.Country;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CountryRepository extends RepositoryBase<Integer, Country> {

    public CountryRepository(EntityManager entityManager) {
        super(entityManager, Country.class);
    }

    @Override
    public List<Country> findAll() {
        return getEntityManager().createQuery("select c from Country c join fetch c.languages", Country.class)
                .getResultList();
    }

}