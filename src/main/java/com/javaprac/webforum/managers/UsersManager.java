package com.javaprac.webforum.managers;

import java.time.LocalDate;
import java.util.List;

import com.javaprac.webforum.model.Message;
import com.javaprac.webforum.model.User;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

public class UsersManager extends Manager<User, Integer> {

    public User getByNickname(String nickname) {
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

    public User getByLogin(String login) {
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

    public User auth(String login, String password) {
        User user = getByLogin(login);
        if (user != null && user.checkPassword(password)) {
            return user;
        } else {
            return null;
        }
    }

    public List<User.UserActivityPair> getUserActivity(User user, LocalDate from , LocalDate till) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<User.UserActivityPair> query = builder.createQuery(User.UserActivityPair.class);

        Root<User> root_user = query.from(User.class);
        ListJoin<User, Message> join_message = root_user.joinList("createdMessages");

        query.where(builder.between(join_message.get("creationTime"), from, till));
        query.where(builder.equal(root_user.get("id"), user.getId()));

        Expression<LocalDate> msg_date = builder.function("date", LocalDate.class, join_message.get("creationTime"));

        query.groupBy(msg_date);
        query.orderBy(builder.asc(msg_date));

        query.multiselect(msg_date, builder.count(join_message.get("id")));

        return em.createQuery(query).getResultList();
    }
}
