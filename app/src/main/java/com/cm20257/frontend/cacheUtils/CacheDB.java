package com.cm20257.frontend.cacheUtils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
abstract public class CacheDB extends RoomDatabase {
    private static volatile CacheDB INSTANCE;

    public abstract CacheDao cacheDao();

    public static CacheDB getDB(Context context) {
        if (INSTANCE == null) {
            synchronized (CacheDB.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        CacheDB.class,
                        "Cache_DB"
                ).allowMainThreadQueries().build(); // allow main thread queries is yabai fix later
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
