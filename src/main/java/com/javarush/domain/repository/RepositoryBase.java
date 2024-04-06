package com.javarush.domain.repository;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public abstract class RepositoryBase<K extends Serializable, E> implements Repository<K, E> {
    private final EntityManager entityManager;
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(K id, Map<String, Object> properties) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        return entityManager.createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList();
    }

    public List<E> findAll(int offset, int limit) {
        return entityManager.createQuery("from " + clazz.getSimpleName(), clazz)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}