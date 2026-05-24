package com.iot.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfid_tags")
public class RfidTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_uid")
    private String tagUid;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public RfidTag() {}

    public Integer getId() { return id; }
    public String getTagUid() { return tagUid; }
    public String getTagName() { return tagName; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getUser() { return user; }

    public void setId(Integer id) { this.id = id; }
    public void setTagUid(String tagUid) { this.tagUid = tagUid; }
    public void setTagName(String tagName) { this.tagName = tagName; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUser(User user) { this.user = user; }
}