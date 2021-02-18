package com.cm20257.frontend.foodDB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import kotlinx.coroutines.CoroutineScope;

@Database(entities = {Food.class}, version = 1)
abstract class FoodDB extends RoomDatabase {
    private static volatile FoodDB INSTANCE;

    public abstract FoodCacheDao foodCacheDao();

    public static FoodDB getDB(Context context, CoroutineScope scope) {
        if (INSTANCE == null) {
            synchronized (FoodDB.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        FoodDB.class,
                        "FoodCacheDB"
                ).build();
            }
        }
        return INSTANCE;
    }

    /*
    private static class Build extends RoomDatabase.Callback {
        private CoroutineScope scope;

        Build(CoroutineScope scope) {
            this.scope = scope;
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

        }
    }*/

}
