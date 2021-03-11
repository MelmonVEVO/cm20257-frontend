package com.cm20257.frontend.cacheUtils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface CacheDao {
    // food DB requests
    @Query("SELECT * FROM food_cache ORDER BY food_name ASC")
    LiveData<List<Food>> getAllFood();

    // add a new thing
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNew(Food newFood);

    // get rid of one thing
    @Delete
    void delete(Food food);

    // only use this in special circumstances (e.g. refreshing the cache)
    @Query("DELETE FROM food_cache")
    void deleteAll();

    // filter out expired food with a given (long) date
    @Query("SELECT * FROM food_cache WHERE expiration > :date")
    LiveData<List<Food>> getNonExpiredFood(long date);

}

