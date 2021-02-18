package com.cm20257.frontend.foodDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface FoodCacheDao {
    @Query("SELECT * FROM food_cache ORDER BY food_name ASC")
    public LiveData<List<Food>> getAllFood();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertNew(Food newFood);

    @Update
    public Completable updateExpense(Food[] args);

    @Delete
    public Completable delete(Food food);
}
