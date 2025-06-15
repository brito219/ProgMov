// app/src/main/java/com/example/ToDo/database/AppDatabase.java
package com.example.ToDo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ToDo.dao.TaskDao;
import com.example.ToDo.entity.Task;
import com.example.ToDo.entity.User;
import com.example.ToDo.database.UserDao;

@Database(entities = {User.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // DAOs
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();

    // singleton
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "todo-db"
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
