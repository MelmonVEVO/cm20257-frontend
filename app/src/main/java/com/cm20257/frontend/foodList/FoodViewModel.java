package com.cm20257.frontend.foodList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cm20257.frontend.cacheUtils.CacheDB;
import com.cm20257.frontend.cacheUtils.CacheDao;
import com.cm20257.frontend.cacheUtils.Food;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {
    private CacheDao dao = CacheDB.getDB(getApplication()).cacheDao();
    LiveData<List<Food>> allFoods = dao.getAllFood();

    synchronized void insert(Food food) {
        dao.insertNew(food);
    }

    synchronized void remove(Food food) {
        dao.delete(food);
    }

    public FoodViewModel(@NonNull Application application) {
        super(application);
    }
}
