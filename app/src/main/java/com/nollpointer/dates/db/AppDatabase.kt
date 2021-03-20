package com.nollpointer.dates.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.db.dao.PractiseDao
import com.nollpointer.dates.db.entity.PractiseEntity

/**
 * БД приложения
 *
 * @author Onanov Aleksey (@onanov)
 */
@Database(entities = [PractiseEntity::class], version = BuildConfig.DB_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun practiseDao(): PractiseDao
}