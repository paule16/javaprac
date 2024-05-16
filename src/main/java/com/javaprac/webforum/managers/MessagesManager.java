package com.javaprac.webforum.managers;

import java.util.List;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import com.javaprac.webforum.model.Message;

public class MessagesManager extends Manager<Message, Integer> {

    public List<Message> searchInDiscussionByContent(int discussion_id, String pattern) {
        SearchSession ss = Search.session(em);

        return ss.search(Message.class)
                .where(f -> f.and(
                    f.match().field("discussion.id").matching(discussion_id),
                    f.match().field("text").matching(pattern)
                ))
                .fetchAllHits();
    }

    public List<Message> searchInSectionByContent(int section_id, String pattern) {
        SearchSession ss = Search.session(em);

        return ss.search(Message.class)
                .where(f -> f.and(
                    f.match().field("discussion.section.id").matching(section_id),
                    f.match().field("text").matching(pattern)
                ))
                .fetchAllHits();
    }

    public List<Message> searchByContent(String pattern) {
        SearchSession ss = Search.session(em);

        return ss.search(Message.class)
                .where(f -> f.match().field("text").matching(pattern))
                .fetchAllHits();
    }
}
