package com.javaprac.db_interface;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class Manager<E, K> {
    EntityManagerFactory emf;

    public Manager()
    {
        emf = Persistence.createEntityManagerFactory("Default");
        if (emf == null) {
            throw new Error("Cannot get EntityManagerFactory");
        }
    }

    public E get(Class<E> entityClass, K key)
    {
        EntityManager em = emf.createEntityManager();
        return em.find(entityClass, key);
    }

    public boolean delete(Class<E> entityClass, K key)
    {
        EntityManager em = emf.createEntityManager();
        E entity = em.find(entityClass, key);
        if (entity == null) return false;
        em.remove(entity);
        return true;
    }

    public void delete(E entity)
    {
        EntityManager em = emf.createEntityManager();
        em.remove(entity);
    }

    public List<E> getAll(Class<E> entityClass)
    {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT * FROM :name", entityClass)
            .setParameter("name", entityClass.getName())
            .getResultList();
    }

    public void add(E entity)
    {
        EntityManager em = emf.createEntityManager();
        em.persist(entity);
    }

    public void commit(E entity)
    {
        EntityManager em = emf.createEntityManager();
        em.merge(entity);
    }

    public void flush()
    {
        EntityManager em = emf.createEntityManager();
        em.flush();
    }
}
