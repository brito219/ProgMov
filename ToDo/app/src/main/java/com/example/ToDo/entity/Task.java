package com.example.ToDo.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE
        )
)
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String description;
    private long dateTime;
    private boolean completed;
    private boolean favorite;
    private String tags;
    private String priority;

    @ColumnInfo(index = true)
    private long userId;

    @Ignore
    public Task() { }

    public Task(String title, String description, long dateTime, String tags) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.tags = tags;
        this.completed = false;
        this.favorite = false;
    }

    // Getters e Setters

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getDateTime() { return dateTime; }
    public void setDateTime(long dateTime) { this.dateTime = dateTime; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    @Override
    public String toString() {
        return title != null ? title : "";
    }
}