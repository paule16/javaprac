package com.javaprac.db_objects;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private User creator;

    private String content;

    private List<String> attachments;

    private Integer likes_num;

    private Integer dislikes_num;

    private LocalDateTime creation_time;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private Discussion discussion;

    private Quote quote;

    public Message(String text,
                   Message quoted,
                   int quote_start,
                   int quote_end,
                   List<String> attachments,
                   Discussion discussion)
    {
        this.content = text;
        this.quote = new Quote(quoted, quote_start, quote_end);
        this.attachments = attachments;
        this.likes_num = 0;
        this.dislikes_num = 0;
        this.creation_time = LocalDateTime.now();
        this.discussion = discussion;
    }

    public User getCreator()
    {
        return creator;
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
class Quote {
    @ManyToOne
    @JoinColumn(name = "quote_msg_id")
    private Message message;

    @Column(name = "quote_start")
    private Integer start;

    @Column(name = "quote_end")
    private Integer end;

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
