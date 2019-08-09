package com.example.todolistmvcapplication.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class QuizDatabase extends RoomDatabase {
    private static QuizDatabase INSTANCE;

    public static QuizDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (QuizDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        QuizDatabase.class, "quiz.db"

                ).build();
            }
        }
        return INSTANCE;
    }

}
