package dev.stefanosansone.randomuserlist.data.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.stefanosansone.randomuserlist.data.database.daos.UserDao

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesUserDao(
        database: UserDatabase,
    ): UserDao = database.userDao()
}