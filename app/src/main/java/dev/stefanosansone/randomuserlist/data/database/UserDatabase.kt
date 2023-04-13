package dev.stefanosansone.randomuserlist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.stefanosansone.randomuserlist.data.database.daos.UserDao
import dev.stefanosansone.randomuserlist.data.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
