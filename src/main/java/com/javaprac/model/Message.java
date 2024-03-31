package com.javaprac.model;

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

    private String content;

    private List<String> attachments;

    private Integer likes_num = 0;

    private Integer dislikes_num = 0;

    private LocalDateTime creation_time;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private Discussion discussion;

    @Embedded
    private Quote quote;

    public boolean equals(Message oth)
    {
        // if (this == oth) {
        //     return true;
        // }

        return oth.id.equals(id) &&
               oth.content.equals(content) &&
               oth.attachments.equals(attachments) &&
               oth.likes_num.equals(likes_num) &&
               oth.dislikes_num.equals(dislikes_num) &&
               oth.creation_time.equals(creation_time) &&
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
        this.content = text;
        this.quote = new Quote(quoted, quote_start, quote_end);
        this.attachments = attachments;
        this.discussion = discussion;
        this.creator = creator;
        this.creation_time = LocalDateTime.now();
    }

    public Message(String text,
                   List<String> attachments,
                   Discussion discussion,
                   User creator)
    {
        this.content = text;
        this.attachments = attachments;
        this.discussion = discussion;
        this.creator = creator;
        this.creation_time = LocalDateTime.now();
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
        return content;
    }

    public String getText(int start, int end)
    {
        return content.substring(start, end);
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

    public int getLikes()
    {
        return likes_num;
    }

    public int getDislikes()
    {
        return dislikes_num;
    }

    public LocalDateTime getCreationTime()
    {
        return creation_time;
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
