package com.example.storeroom3.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storeroom3.common.entities.StoreEntity

@Database(entities = [StoreEntity::class], version = 2)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}