package com.example.ToDo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ToDo.entity.User;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

   @Update
    void update(User user); // Permite atualizar nome, email e senha

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User findById(long id); // Necessário para buscar o usuário logado

    @Query("SELECT * FROM users LIMIT 1")
    User getAnyUser();
}