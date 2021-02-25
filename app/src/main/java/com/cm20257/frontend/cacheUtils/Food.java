package com.cm20257.frontend.cacheUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_cache")
public class Food {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") public int uid;
    @ColumnInfo(name = "food_name") public String foodName;
    @ColumnInfo(name = "quantity") public float quantity;
    @ColumnInfo(name = "quantity_type") public String quantityUnit;
    @ColumnInfo(name = "expiration") public long expiration;
}
