package com.securevault.dto;

public class CredentialResponse {

    private Long id;
    private String title;
    private String username;
    private String password;
    private String notes;
    private Long categoryId;

    public CredentialResponse() {
    }

    public CredentialResponse(
            Long id,
            String title,
            String username,
            String password,
            String notes,
            Long categoryId) {

        this.id = id;
        this.title = title;
        this.username = username;
        this.password = password;
        this.notes = notes;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNotes() {
        return notes;
    }

    public Long getCategoryId() {
        return categoryId;
    }


}