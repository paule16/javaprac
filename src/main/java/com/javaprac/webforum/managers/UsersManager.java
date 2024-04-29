package com.javaprac.webforum.managers;

import com.javaprac.webforum.model.User;

import jakarta.persistence.NoResultException;

public class UsersManager extends Manager<User, Integer> {

    public User getByNickname(String nickname)
    {
        em.getTransaction().begin();
        User result;
        try {
            result = em.createQuery("SELECT u FROM User u WHERE u.nickname = :nick", User.class)
                .setParameter("nick", nickname)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.getTransaction().commit();
        }
        return result;
    }

    public User getByLogin(String login)
    {
        em.getTransaction().begin();
        User result;
        try {
            result = em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.getTransaction().commit();
        }
        return result;
    }

    public User auth(String login, String password)
    {
        User user = getByLogin(login);
        if (user != null && user.checkPassword(password)) {
            return user;
        } else {
            return null;
        }
    }
}
