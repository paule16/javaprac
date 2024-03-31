package com.javaprac.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.javaprac.Permission;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Discussions")
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Discussion_seq")
    @SequenceGenerator(allocationSize = 1, name = "Discussion_seq")
    private Integer id;

    private String label;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private User creator;

    private LocalDateTime creation_time;
    
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private Section section;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Permission> permissions;

    @OneToMany(mappedBy = "discussion")
    List<Message> messages = new ArrayList<>();


    public boolean equals(Discussion oth)
    {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
               oth.label.equals(label) &&
               oth.description.equals(description) &&
               oth.creation_time.equals(creation_time) &&
               oth.permissions.equals(permissions) &&
               oth.creator.equals(creator);
    }

    public Discussion() {}

    public Discussion(String label,
                      String description,
                      Map<String, Permission> perm,
                      Section section,
                      User creator)
    {
        this.label = label;
        this.description = description;
        this.permissions = perm;
        this.section = section;
        this.creator = creator;
        this.creation_time = LocalDateTime.now();
    }

    public Discussion(String label,
                      String description,
                      Section section,
                      User creator)
    {
        this.label = label;
        this.description = description;
        this.permissions = Map.of();
        this.section = section;
        this.creator = creator;
        this.creation_time = LocalDateTime.now();
    }

    public int getId()
    {
        return id;
    }

    public String getTheme()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }

    public User getCreator()
    {
        return creator;
    }

    public Section getSection()
    {
        return section;
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public LocalDateTime getCreationTime()
    {
        return creation_time;
    }

    public boolean canRead(User user)
    {
        if (user.getRoles().contains("admin") || user == creator)
        {
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

    public boolean canWrite(User user)
    {
        if (user.getRoles().contains("admin") || user == creator) {
            return true;
        }

        if (user.isBanned()) {
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

    public boolean canEdit(User user)
    {
        if (user.getRoles().contains("admin") || user == creator) {
            return true;
        }

        if (user.isBanned()) {
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
}
