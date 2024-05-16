package com.javaprac.webforum.model;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Attachments_seq")
    @SequenceGenerator(allocationSize = 1, name = "Attachments_seq")
    private Integer id;

    @ManyToOne
    @JoinColumn(updatable = false)
    Message message;

    String name;

    byte[] content;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Message getMessage() {
        return message;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Attachment() {
    }

    public Attachment(MultipartFile file, Message message) throws IOException {
        name = file.getOriginalFilename();
        content = file.getBytes().clone();

        this.message = message;
    }
}
