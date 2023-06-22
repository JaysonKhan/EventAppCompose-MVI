package uz.gita.eventappcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.eventappcompose.data.local.dao.MyDao
import uz.gita.eventappcompose.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase:RoomDatabase() {
    abstract fun getDatabaseDao():MyDao

    companion object{
        val DB_NAME = "database"
    }

}