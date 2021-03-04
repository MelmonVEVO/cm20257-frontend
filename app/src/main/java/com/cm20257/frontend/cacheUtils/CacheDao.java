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
    public LiveData<List<Food>> getAllFood();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNew(Food newFood);

    @Delete
    public void delete(Food food);
}
