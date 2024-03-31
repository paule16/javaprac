package com.javaprac.managers;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import jakarta.persistence.EntityManager;

public abstract class Manager<E, K> {
    EntityManager em;

    Manager()
    {
        em = EntityManagerCreator.create();
    }

    public E get(Class<E> entityClass, K key)
    {
        em.getTransaction().begin();
        E result = em.find(entityClass, key);
        em.getTransaction().commit();
        return result;
    }

    public void delete(E entity)
    {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }

    public List<E> getAll(Class<E> entityClass)
    {
        em.getTransaction().begin();
        List<E> result = em.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
            .getResultList();
        em.getTransaction().commit();
        return result;
    }

    public void add(E entity)
    {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    public void commit(E entity)
    {
        EntityManager em = EntityManagerCreator.create();
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
    }

    public void refresh(E entity)
    {
        em.refresh(entity);
    }
}

class EntityManagerCreator {
    static SessionFactory sf;

    static SessionFactory getFactory()
    {
        if (sf == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

            sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }

        return sf;
    }

    public static EntityManager create()
    {
        return getFactory().createEntityManager();
    }
}
