package com.example.ToDo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ToDo.entity.Task;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY dateTime ASC")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY dateTime ASC")
    List<Task> getPending();

    @Query("SELECT * FROM tasks WHERE favorite = 1 ORDER BY dateTime ASC")
    List<Task> getFavorites();

    @Query("SELECT * FROM tasks WHERE tags LIKE '%' || :tag || '%' ORDER BY dateTime ASC")
    List<Task> getByTag(String tag);

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    List<Task> search(String query);
}
