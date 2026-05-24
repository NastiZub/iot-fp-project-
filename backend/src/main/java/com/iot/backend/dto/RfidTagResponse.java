package com.iot.backend.dto;

import java.time.LocalDateTime;

public class RfidTagResponse {

    private Integer id;
    private String tagUid;
    private String tagName;
    private LocalDateTime createdAt;

    public RfidTagResponse() {}

    public RfidTagResponse(Integer id, String tagUid, String tagName, LocalDateTime createdAt) {
        this.id = id;
        this.tagUid = tagUid;
        this.tagName = tagName;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public String getTagUid() { return tagUid; }
    public String getTagName() { return tagName; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Integer id) { this.id = id; }
    public void setTagUid(String tagUid) { this.tagUid = tagUid; }
    public void setTagName(String tagName) { this.tagName = tagName; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}