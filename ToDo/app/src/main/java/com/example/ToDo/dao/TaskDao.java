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

    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY dateTime ASC")
    List<Task> getAll(long userId);

    @Query("SELECT * FROM tasks WHERE completed = 0 AND userId = :userId ORDER BY dateTime ASC")
    List<Task> getPending(long userId);

    @Query("SELECT * FROM tasks WHERE completed = 1 AND userId = :userId ORDER BY dateTime ASC")
    List<Task> getCompleted(long userId);

    @Query("SELECT * FROM tasks WHERE favorite = 1 AND userId = :userId ORDER BY dateTime ASC")
    List<Task> getFavorites(long userId);

    @Query("SELECT * FROM tasks WHERE tags LIKE '%' || :tag || '%' AND userId = :userId ORDER BY dateTime ASC")
    List<Task> getByTag(String tag, long userId);

    @Query("SELECT * FROM tasks WHERE userId = :userId AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')")
    List<Task> search(String query, long userId);
    @Query("SELECT * FROM tasks WHERE userId = :id AND dateTime BETWEEN :inicio AND :fim")
    List<Task> getByDateRange(long id, long inicio, long fim);
    @Query("SELECT * FROM tasks WHERE userId = :id ORDER BY CASE priority WHEN 'alta' THEN 1 WHEN 'média' THEN 2 WHEN 'baixa' THEN 3 ELSE 4 END")
    List<Task> getAllOrderedByPriority(long id);

}