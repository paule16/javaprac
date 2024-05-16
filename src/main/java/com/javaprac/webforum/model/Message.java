package com.javaprac.webforum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Messages")
@Indexed
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Message_seq")
    @SequenceGenerator(allocationSize = 1, name = "Message_seq")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private User creator;

    @Column(name = "content")
    @FullTextField
    private String text;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    List<Attachment> attachments = new ArrayList<>();

    private Integer likesNum = 0;

    private Integer dislikesNum = 0;

    private LocalDateTime creationTime;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Discussion discussion;

    @ManyToOne
    @JoinColumn(name = "quote_msg_id")
    private Message quoted;

    @OneToMany(mappedBy = "quoted")
    private List<Message> quoters = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Likes", joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> likers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Dislikes", joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> dislikers = new ArrayList<>();

    public boolean equals(Message oth) {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
                oth.text.equals(text) &&
                oth.attachments.equals(attachments) &&
                oth.likesNum.equals(likesNum) &&
                oth.dislikesNum.equals(dislikesNum) &&
                oth.creationTime.equals(creationTime) &&
                oth.quoted.equals(oth.quoted) &&
                oth.discussion.equals(discussion) &&
                oth.creator.equals(creator);
    }

    public Message() {
    }

    public Message(String text,
            Message quoted,
            Discussion discussion,
            User creator) {
        this.text = text;
        this.quoted = quoted;
        this.discussion = discussion;
        this.creator = creator;
        this.creationTime = LocalDateTime.now();
    }

    public Message(String text,
            Discussion discussion,
            User creator) {
        this.text = text;
        this.discussion = discussion;
        this.creator = creator;
        this.creationTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public Discussion getDiscussion() {
        return discussion;
    }

    public String getText() {
        return text;
    }

    public Message getQuoted() {
        return quoted;
    }

    public List<Message> getQuoters() {
        return quoters;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public int like(User user) {
        for (int i = 0; i < likers.size(); i++) {
            if (likers.get(i).equals(user)) {
                likesNum--;
                likers.remove(i);
                return -1;
            }
        }
        likesNum++;
        likers.add(user);
        return 1;
    }

    public int getDislikesNum() {
        return dislikesNum;
    }

    public int dislike(User user) {
        for (int i = 0; i < dislikers.size(); i++) {
            if (dislikers.get(i).equals(user)) {
                dislikesNum--;
                dislikers.remove(i);
                return -1;
            }
        }
        dislikesNum++;
        dislikers.add(user);
        return 1;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
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
