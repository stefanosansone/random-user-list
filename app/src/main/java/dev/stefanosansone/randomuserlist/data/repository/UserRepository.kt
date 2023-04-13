package dev.stefanosansone.randomuserlist.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.stefanosansone.randomuserlist.data.database.UserDatabase
import dev.stefanosansone.randomuserlist.data.database.daos.UserDao
import dev.stefanosansone.randomuserlist.data.network.ApiInterface
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UserRepository @Inject constructor(
    private val database: UserDatabase,
    private val apiInterface: ApiInterface,
    private val userDao: UserDao
) {
    fun getUsers() = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = UserRemoteMediator(database, apiInterface, userDao)
    ){
        database.userDao().pagingSource()
    }.flow
}