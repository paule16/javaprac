package com.javaprac.webforum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.type.SqlTypes;

import com.javaprac.webforum.Permission;
import com.javaprac.webforum.managers.DiscussionsManager;
import com.javaprac.webforum.managers.MessagesManager;
import com.javaprac.webforum.managers.SectionsManager;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Indexed
@Table(name = "Sections")
public class Section {
    @Id
    @GeneratedValue(generator = "Section_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "Section_seq", allocationSize = 1)
    private Integer id;

    @FullTextField
    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Permission> permissions;

    @ManyToMany
    @JoinTable(name = "BannedInSection", joinColumns = @JoinColumn(name = "section_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> banned_users = new ArrayList<>();

    @OneToMany(mappedBy = "section")
    List<Discussion> discussions = new ArrayList<>();

    public boolean equals(Section oth) {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
                oth.name.equals(name) &&
                oth.description.equals(description) &&
                oth.permissions.equals(permissions);
    }

    public Section() {
    }

    public Section(String name, String description, Map<String, Permission> perm) {
        this.name = name;
        this.description = description;
        this.permissions = perm;
    }

    public Section(String name, String description) {
        this.name = name;
        this.description = description;
        this.permissions = Map.of();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isBanned(User user) {

        for (int i = 0; i < banned_users.size(); i++) {
            if (banned_users.get(i).equals(user)) {
                return true;
            }
        }
        return false;
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public boolean publicAccessible() {
        return permissions.isEmpty() || permissions.containsKey("public");
    }

    public boolean canRead(User user) {
        if (user.getRoles().contains("admin")) {
            return true;
        }

        if (permissions.isEmpty() || permissions.containsKey("public")) {
            return true;
        }

        for (String role : user.getRoles()) {
            if (permissions.containsKey(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean canWrite(User user) {
        if (user.getRoles().contains("admin")) {
            return true;
        }

        if (user.getBannedGlobal() || banned_users.contains(user)) {
            return false;
        }

        if (permissions.isEmpty()) {
            return false;
        }

        if (permissions.containsKey("public") && permissions.get("public").allow_write()) {
            return true;
        }

        for (String role : user.getRoles()) {
            if (permissions.containsKey(role) && permissions.get(role).allow_write()) {
                return true;
            }
        }
        return false;
    }

    public boolean canEdit(User user) {
        if (user.getRoles().contains("admin")) {
            return true;
        }

        if (user.getBannedGlobal() || banned_users.contains(user)) {
            return false;
        }

        if (permissions.isEmpty()) {
            return false;
        }

        if (permissions.containsKey("public") && permissions.get("public").allow_edit()) {
            return true;
        }

        for (String role : user.getRoles()) {
            if (permissions.containsKey(role) && permissions.get(role).allow_edit()) {
                return true;
            }
        }
        return false;
    }

    public boolean canDelete(User user) {
        return user.isAdmin();
    }

    public void ban(User user) {
        banned_users.add(user);
    }

    public void unban(User user) {
        for (int i = 0; i < banned_users.size(); i++) {
            if (banned_users.get(i).equals(user)) {
                banned_users.remove(i);
                break;
            }
        }
    }

    public static class ActiveUsersWrapper {
        public User user;
        public LocalDateTime minCreationTime;
        public LocalDateTime maxCreationTime;
        public Long msgNum;

        public User getUser() {
            return user;
        }

        public LocalDateTime getMinCreationTime() {
            return minCreationTime;
        }

        public LocalDateTime getMaxCreationTime() {
            return maxCreationTime;
        }

        public Long getMsgNum() {
            return msgNum;
        }

        public ActiveUsersWrapper(User user, LocalDateTime min_time, LocalDateTime max_time, Long msgNum) {
            this.user = user;
            this.minCreationTime = min_time;
            this.maxCreationTime = max_time;
            this.msgNum = msgNum;
        }
    }

    public List<ActiveUsersWrapper> getActiveUsers(LocalDateTime from, LocalDateTime to) {
        return (new SectionsManager()).getActiveUsers(getId(), from, to);
    }

    public List<Discussion> searchDiscussionsByName(String pattern) {
        DiscussionsManager dm = new DiscussionsManager();
        return dm.searchInSectionByName(getId(), pattern);
    }

    public List<Message> searchMessagesByContent(String pattern) {
        MessagesManager mm = new MessagesManager();
        return mm.searchInSectionByContent(getId(), pattern);
    }
}
