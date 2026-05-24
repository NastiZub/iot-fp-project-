package com.iot.backend.dto;

public class RegisterRfidRequest {

    private Integer userId;
    private String tagUid;
    private String tagName;

    public RegisterRfidRequest() {}

    public Integer getUserId() {
        return userId;
    }

    public String getTagUid() {
        return tagUid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTagUid(String tagUid) {
        this.tagUid = tagUid;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}