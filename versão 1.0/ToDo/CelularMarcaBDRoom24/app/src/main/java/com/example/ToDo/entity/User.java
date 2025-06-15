package com.example.ToDo.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String email;
    private String passwordHash;
    private String photoUri;

    public User(String name, String email, String passwordHash, String photoUri) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.photoUri = photoUri;
    }

    // Getters & Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }
}
