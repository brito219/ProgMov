package com.example.ToDo.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ToDo.entity.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    // busca email sem diferenciar maiúsculas/minúsculas
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    User findByEmail(String email);
}
