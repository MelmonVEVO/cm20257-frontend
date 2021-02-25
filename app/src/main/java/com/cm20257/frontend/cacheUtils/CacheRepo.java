package com.cm20257.frontend.cacheUtils;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CacheRepo {
    private CacheDao dao;
    LiveData<List<Food>> allFood = dao.getAllFood();

    synchronized void insert(Food food) {
        dao.insertNew(food);
    }
}
