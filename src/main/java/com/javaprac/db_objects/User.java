package com.javaprac.db_objects;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @NaturalId
    private String nickname;

    @NaturalId
    private String login;

    private String password;

    private LocalDate date_of_registration;

    private List<String> roles;

    private Boolean banned_global;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Discussion> created_discussions;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @OrderColumn(name = "creation_time", insertable = true, updatable = false)
    private List<Message> created_messages;

    public User(String nickname, String login, String password, List<String> roles)
    {
        this.nickname = nickname;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.date_of_registration = LocalDate.now();
        this.banned_global = false;
    }

    public boolean checkPassword(String password)
    {
        return password == this.password;
    }

    public int getId()
    {
        return id;
    }

    public String getNick()
    {
        return nickname;
    }

    public String getLogin()
    {
        return login;
    }

    public LocalDate getRegistrationDate()
    {
        return date_of_registration;
    }

    public List<String> getRoles()
    {
        return roles;
    }

    public List<Discussion> getCreateDiscussions()
    {
        return created_discussions;
    }

    public boolean isBanned()
    {
        return banned_global;
    }

    public void ban()
    {
        banned_global = true;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setNick(String nickname)
    {
        this.nickname = nickname;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
