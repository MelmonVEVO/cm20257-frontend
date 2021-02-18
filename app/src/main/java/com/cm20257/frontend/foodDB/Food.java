package com.cm20257.frontend.foodDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_cache")
public class Food {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") int uid;
    @ColumnInfo(name = "food_name") String foodName;
    @ColumnInfo(name = "quantity") float quantity;
    @ColumnInfo(name = "quantity_type") String quantityType;
    @ColumnInfo(name = "expiration") long expiration;
}
