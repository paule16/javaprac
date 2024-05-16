package com.javaprac.webforum.managers;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import com.javaprac.webforum.model.Discussion;
import com.javaprac.webforum.model.Message;
import com.javaprac.webforum.model.Section;
import com.javaprac.webforum.model.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;

public class SectionsManager extends Manager<Section, Integer> {

    public List<Section.ActiveUsersWrapper> getActiveUsers(int section_id, LocalDateTime from, LocalDateTime to) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Section.ActiveUsersWrapper> query = builder.createQuery(Section.ActiveUsersWrapper.class);

        Root<Section> section_root = query.from(Section.class);
        ListJoin<Section, Discussion> discussions_join = section_root.joinList("discussions");
        ListJoin<Discussion, Message> messages_join = discussions_join.joinList("messages");
        Join<Message, User> user_join = messages_join.join("creator");

        query.groupBy(user_join.get("id"));

        if (from != null && to != null) {
            query.where(builder.between(messages_join.get("creationTime"), from, to));
        }

        query.where(builder.equal(section_root.get("id"), section_id));

        query.multiselect(user_join,
                builder.min(messages_join.get("creationTime")),
                builder.max(messages_join.get("creationTime")),
                builder.count(messages_join));

        return em.createQuery(query).getResultList();
    }

    public List<Section> searchByName(String pattern) {
        SearchSession s = Search.session(em);

        return s.search(Section.class)
                .where(f -> f.match().field("name").matching(pattern))
                .fetchAllHits();

    }
}
