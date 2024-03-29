package com.javaprac.db_objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Section {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(value = EnumType.ORDINAL)
    private Map<String, Permission> permissions;

    @ManyToMany
    @JoinTable(
        name = "BannedInSection",
        joinColumns =
            @JoinColumn(name = "section_id", referencedColumnName = "id"),
        inverseJoinColumns = 
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private List<User> banned_users = new ArrayList<>();

    @OneToMany(mappedBy = "section")
    List<Discussion> discussions;

    public Section(String name, String description, Map<String, Permission> perm)
    {
        this.name = name;
        this.description = description;
        this.permissions = perm;
    }

    public Section(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isBanned(User user)
    {
        return banned_users.contains(user);
    }

    public List<Discussion> getDiscussions()
    {
        return discussions;
    }

    public boolean can_read(User user)
    {
        if (user.getRoles().contains("admin"))
        {
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

    public boolean can_write(User user)
    {
        if (user.getRoles().contains("admin"))
        {
            return true;
        }

        if (banned_users.contains(user)) {
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

    public boolean can_edit(User user)
    {
        if (user.getRoles().contains("admin"))
        {
            return true;
        }

        if (banned_users.contains(user)) {
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

    public void ban(User user)
    {
        banned_users.add(user);
    }
}
