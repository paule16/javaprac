package com.javaprac.managers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.javaprac.model.Section;

import jakarta.persistence.EntityManager;

public class SectionsManager extends Manager<Section, Integer> {

    public List<Object[]> getActiveUsers(int section_id, LocalDate from, LocalDate to)
    {
        EntityManager em = EntityManagerCreator.create();
        return em.createQuery(
            "SELECT u.id, u.nickname, min(m.creation_time), max(m.creation_time), " +
                "count(*), u.banned " +
            "FROM Section s " +
                "NATURAL JOIN Discussions d " +
                "NATURAL JOIN Message m " +
                "INNER JOIN User u ON (m.creator_id = u.id) " +
            "GROUP BY u.id " +
            "WHERE m.creation_time > :start " +
                "AND m.creation_time < :end",
            Object[].class
        )
            .setParameter("start", LocalDateTime.from(from))
            .setParameter("end", LocalDateTime.from(to))
            .getResultList();
    }

}
