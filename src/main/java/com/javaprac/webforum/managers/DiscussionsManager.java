package com.javaprac.webforum.managers;

import java.util.List;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import com.javaprac.webforum.model.Discussion;

public class DiscussionsManager extends Manager<Discussion, Integer> {

    public List<Discussion> searchInSectionByName(int section_id, String pattern) {
        SearchSession ss = Search.session(em);

        return ss.search(Discussion.class)
                .where(f -> f.and(
                    f.match().field("section.id").matching(section_id),
                    f.match().field("name").matching(pattern)
                ))
                .fetchAllHits();
    }
}
