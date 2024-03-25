package com.javaprac.db_interface;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;

import com.javaprac.db_objects.Discussion;
import com.javaprac.db_objects.User;

import jakarta.persistence.EntityManager;

public class UsersManager extends Manager<User, Integer> {

    public User getByNickname(String nickname)
    {
        EntityManager em = emf.createEntityManager();
        return em.unwrap(Session.class)
            .byNaturalId(User.class)
            .using("nickname", nickname)
            .load();
    }

    public User getByLogin(String login)
    {
        EntityManager em = emf.createEntityManager();
        return em.unwrap(Session.class)
            .byNaturalId(User.class)
            .using("login", login)
            .load();
    }

    public User auth(String login, String password)
    {
        EntityManager em = emf.createEntityManager();
        User user = em.unwrap(Session.class)
            .byNaturalId(User.class)
            .using("login", login)
            .load();
        if (user.checkPassword(password)) {
            return user;
        } else {
            return null;
        }
    }
}
