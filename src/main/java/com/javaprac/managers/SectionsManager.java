package com.javaprac.managers;

import java.time.LocalDateTime;
import java.util.List;

import com.javaprac.model.Discussion;
import com.javaprac.model.Message;
import com.javaprac.model.Section;
import com.javaprac.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;

public class SectionsManager extends Manager<Section, Integer> {

    public List<Section.ActiveUsersWrapper> getActiveUsers(int section_id, LocalDateTime from, LocalDateTime to)
    {
        EntityManager em = EntityManagerCreator.create();

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Section.ActiveUsersWrapper> query = builder.createQuery(Section.ActiveUsersWrapper.class);

        Root<Section> section_root = query.from(Section.class);
        ListJoin<Section, Discussion> discussions_join = section_root.joinList("discussions");
        ListJoin<Discussion, Message> messages_join = discussions_join.joinList("messages");
        Join<Message, User> user_join = messages_join.join("creator");

        query.groupBy(user_join.get("id"));

        query.where(builder.between(messages_join.get("creation_time"), from, to));

        query.multiselect(user_join,
                          builder.min(messages_join.get("creation_time")),
                          builder.max(messages_join.get("creation_time")),
                          builder.count(messages_join));

        return em.createQuery(query).getResultList();
    }

}
