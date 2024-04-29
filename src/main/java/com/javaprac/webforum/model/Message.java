package com.javaprac.webforum.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Message_seq")
    @SequenceGenerator(allocationSize = 1, name = "Message_seq")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private User creator;

    @Column(name = "content")
    private String text;

    private List<String> attachments;

    private Integer likesNum = 0;

    private Integer dislikesNum = 0;

    private LocalDateTime creationTime;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private Discussion discussion;

    @Embedded
    private Quote quote;

    public boolean equals(Message oth)
    {
        if (this == oth) {
            return true;
        }

        return oth.id.equals(id) &&
               oth.text.equals(text) &&
               oth.attachments.equals(attachments) &&
               oth.likesNum.equals(likesNum) &&
               oth.dislikesNum.equals(dislikesNum) &&
               oth.creationTime.equals(creationTime) &&
               ((oth.quote == null && quote == null) || oth.quote.equals(quote)) &&
               oth.discussion.equals(discussion) &&
               oth.creator.equals(creator);
    }

    public Message() {}

    public Message(String text,
                   Message quoted,
                   int quote_start,
                   int quote_end,
                   List<String> attachments,
                   Discussion discussion,
                   User creator)
    {
        this.text = text;
        this.quote = new Quote(quoted, quote_start, quote_end);
        this.attachments = attachments;
        this.discussion = discussion;
        this.creator = creator;
        this.creationTime = LocalDateTime.now();
    }

    public Message(String text,
                   List<String> attachments,
                   Discussion discussion,
                   User creator)
    {
        this.text = text;
        this.attachments = attachments;
        this.discussion = discussion;
        this.creator = creator;
        this.creationTime = LocalDateTime.now();
    }

    public int getId()
    {
        return id;
    }

    public User getCreator()
    {
        return creator;
    }

    public Discussion getDiscussion()
    {
        return discussion;
    }

    public String getText()
    {
        return text;
    }

    public String getText(int start, int end)
    {
        return text.substring(start, end);
    }

    public Message getQuoted()
    {
        if (quote == null) {
            return null;
        } else {
            return quote.getMessage();
        }
    }

    public String getQuote()
    {
        if (quote == null) {
            return null;
        } else {
            return quote.getQuote();
        }
    }

    public List<String> getAttachments()
    {
        return attachments;
    }

    public int getLikesNum()
    {
        return likesNum;
    }

    public int getDislikesNum()
    {
        return dislikesNum;
    }

    public LocalDateTime getCreationTime()
    {
        return creationTime;
    }

}

@Embeddable
class Quote implements Serializable {
    @ManyToOne
    @JoinColumn(name = "quote_msg_id")
    private Message message;

    @Column(name = "quote_start")
    private Integer start;

    @Column(name = "quote_end")
    private Integer end;

    public boolean equals(Quote oth)
    {
        // if (this == oth) {
        //     return true;
        // }

        return oth.start.equals(start) &&
               oth.end.equals(end) &&
               oth.message.equals(message);
    }

    public Quote() {}

    public Quote(Message message, int start, int end)
    {
        this.message = message;
        this.start = start;
        this.end = end;
    }

    public Message getMessage()
    {
        return message;
    }

    public String getQuote()
    {
        return message.getText(start, end);
    }
}
