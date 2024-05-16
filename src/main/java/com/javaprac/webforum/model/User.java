package com.javaprac.webforum.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.javaprac.webforum.managers.UsersManager;

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

    private LocalDate registrationDate;

    private List<String> roles = new ArrayList<>();

    private Boolean bannedGlobal = false;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @OrderBy("creationTime DESC")
    private List<Discussion> createdDiscussions = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @OrderBy("creationTime DESC")
    private List<Message> createdMessages = new ArrayList<>();

    public boolean equals(User oth) {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
                oth.nickname.equals(nickname) &&
                oth.login.equals(login) &&
                oth.password.equals(password) &&
                oth.registrationDate.equals(registrationDate) &&
                oth.roles.equals(roles) &&
                oth.bannedGlobal.equals(bannedGlobal);
    }

    public User() {
    }

    public User(String nickname, String login, String password, List<String> roles) {
        this.nickname = nickname;
        this.login = login;
        this.password = password;
        this.roles = roles == null ? List.of() : roles;
        this.registrationDate = LocalDate.now();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isAdmin() {
        return roles.contains("admin");
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<Discussion> getCreateDiscussions() {
        return createdDiscussions;
    }

    public List<Message> getCreatedMessages() {
        return createdMessages;
    }

    public boolean getBannedGlobal() {
        return bannedGlobal;
    }

    public void setBannedGlobal(boolean banned) {
        bannedGlobal = banned;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNick(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserActivityPair> getActivity(LocalDate from, LocalDate till) {
        UsersManager um = new UsersManager();
        return um.getUserActivity(this, from, till);
    }

    public static class UserActivityPair {
        String day;
        Integer number;
    
        private UserActivityPair(String day, Integer number) {
            this.day = day;
            this.number = number;
        }

        private static String stringifyDate(LocalDate date) {
            Integer day = date.getDayOfMonth();
            Integer month = date.getMonthValue();

            return String.format("%02d.%02d", day, month);
        }

        public UserActivityPair(LocalDate date, Long number) {
            this.day = stringifyDate(date);
            this.number = number.intValue();
        }
    
        public static UserActivityPair of(String day, Integer number) {
            return new UserActivityPair(day, number);
        }

        public boolean dayIs(LocalDate date) {
            return this.day.equals(stringifyDate(date));
        }
    
        public String getDay() {
            return day;
        }
    
        public void setDay(String day) {
            this.day = day;
        }
    
        public Integer getNumber() {
            return number;
        }
    
        public void setNumber(Integer number) {
            this.number = number;
        }
    }
}
