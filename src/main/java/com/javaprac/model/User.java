package com.javaprac.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(generator = "User_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "User_seq", allocationSize = 1)
    private Integer id;

    private String nickname;

    private String login;

    private String password;

    private LocalDate date_of_registration;

    private List<String> roles = new ArrayList<>();

    private Boolean banned_global = false;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @OrderBy("creation_time")
    private List<Discussion> created_discussions = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @OrderBy("creation_time")
    private List<Message> created_messages = new ArrayList<>();

    public boolean equals(User oth)
    {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
               oth.nickname.equals(nickname) &&
               oth.login.equals(login) &&
               oth.password.equals(password) &&
               oth.date_of_registration.equals(date_of_registration) &&
               oth.roles.equals(roles) &&
               oth.banned_global.equals(banned_global);
    }

    public User() {}

    public User(String nickname, String login, String password, List<String> roles)
    {
        this.nickname = nickname;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.date_of_registration = LocalDate.now();
    }

    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
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


    public List<Message> getCreatedMessages()
    {
        return created_messages;
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
