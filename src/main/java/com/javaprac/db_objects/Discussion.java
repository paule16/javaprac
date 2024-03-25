package com.javaprac.db_objects;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Discussion {
    @Id
    @GeneratedValue
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
    List<Message> messages;

    public Discussion(String label, String description, Map<String, Permission> perm)
    {
        this.label = label;
        this.description = description;
        this.permissions = perm;
        this.creation_time = LocalDateTime.now();
    }

    public Discussion(String label, String description)
    {
        this.label = label;
        this.description = description;
        this.creation_time = LocalDateTime.now();
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

    public List<Message> getMessages()
    {
        return messages;
    }

    public LocalDateTime getCreationTime()
    {
        return creation_time;
    }

    public boolean can_read(User user)
    {
        if (user.getRoles().contains("admin"))
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

    public boolean can_write(User user)
    {
        if (user.getRoles().contains("admin"))
        {
            return true;
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

    public boolean can_edit(User user)
    {
        if (user.getRoles().contains("admin"))
        {
            return true;
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
