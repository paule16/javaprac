package com.javaprac.webforum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.type.SqlTypes;

import com.javaprac.webforum.Permission;
import com.javaprac.webforum.managers.MessagesManager;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Discussions")
@Indexed
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Discussion_seq")
    @SequenceGenerator(allocationSize = 1, name = "Discussion_seq")
    private Integer id;

    @FullTextField
    private String name;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private User creator;

    private LocalDateTime creationTime;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Section section;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Permission> permissions;

    @OneToMany(mappedBy = "discussion", fetch = FetchType.LAZY)
    @OrderBy("creationTime DESC")
    List<Message> messages = new ArrayList<>();

    public boolean equals(Discussion oth) {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
                oth.name.equals(name) &&
                oth.description.equals(description) &&
                oth.creationTime.equals(creationTime) &&
                oth.permissions.equals(permissions) &&
                oth.creator.equals(creator);
    }

    public Discussion() {
    }

    public Discussion(String label,
            String description,
            Map<String, Permission> perm,
            Section section,
            User creator) {
        this.name = label;
        this.description = description;
        this.permissions = perm;
        this.section = section;
        this.creator = creator;
        this.creationTime = LocalDateTime.now();
    }

    public Discussion(String label,
            String description,
            Section section,
            User creator) {
        this.name = label;
        this.description = description;
        this.permissions = Map.of();
        this.section = section;
        this.creator = creator;
        this.creationTime = LocalDateTime.now();
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

    public User getCreator() {
        return creator;
    }

    public Section getSection() {
        return section;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Map<String, Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean publicAccessible() {
        return permissions.isEmpty() || permissions.containsKey("public");
    }

    public boolean canRead(User user) {
        if (user.getRoles().contains("admin") || user.equals(creator)) {
            return true;
        }

        if (permissions.isEmpty()) {
            return true;
        }

        if (permissions.containsKey("public")) {
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
        if (user.getRoles().contains("admin") || user.equals(creator)) {
            return true;
        }

        if (user.getBannedGlobal() || section.isBanned(user)) {
            return false;
        }

        for (String role : user.getRoles()) {
            if (permissions.containsKey(role) && permissions.get(role).allow_write()) {
                return true;
            }
        }

        if (section.canWrite(user)) {
            return true;
        }

        if (permissions.containsKey("public") && permissions.get("public").allow_write()) {
            return true;
        }

        return false;
    }

    public boolean canEdit(User user) {
        if (user.getRoles().contains("admin") || user.equals(creator)) {
            return true;
        }

        if (user.getBannedGlobal() || section.isBanned(user)) {
            return false;
        }

        for (String role : user.getRoles()) {
            if (permissions.containsKey(role) && permissions.get(role).allow_edit()) {
                return true;
            }
        }

        if (section.canEdit(user)) {
            return true;
        }

        if (permissions.containsKey("public") && permissions.get("public").allow_edit()) {
            return true;
        }

        return false;
    }

    public boolean canDelete(User user) {
        return section.canEdit(user) || user.equals(creator);
    }

    public List<Message> searchMessagesByContent(String content) {
        MessagesManager mm = new MessagesManager();

        return mm.searchInDiscussionByContent(getId(), content);
    }

    public String printCreationTime() {
        return String.format(
                "%02d:%02d %02d.%02d.%04d",
                creationTime.getHour(),
                creationTime.getMinute(),
                creationTime.getDayOfMonth(),
                creationTime.getMonthValue(),
                creationTime.getYear());
    }
}
