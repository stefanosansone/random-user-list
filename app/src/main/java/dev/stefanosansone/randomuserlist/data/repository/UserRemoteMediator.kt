package dev.stefanosansone.randomuserlist.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.stefanosansone.randomuserlist.data.database.UserDatabase
import dev.stefanosansone.randomuserlist.data.database.daos.UserDao
import dev.stefanosansone.randomuserlist.data.model.UserEntity
import dev.stefanosansone.randomuserlist.data.network.ApiInterface
import retrofit2.HttpException
import java.io.IOException

/**
 * Mediates between the [ApiInterface] and [UserDao] to load pages of [UserEntity] objects
 * into a [PagingData] stream. Uses the [RemoteMediator] interface to fetch data from the API
 * or local database when necessary.
 *
 * @param database The [UserDatabase] to use for local caching of data.
 * @param apiInterface The [ApiInterface] to use for fetching data from the API.
 * @param userDao The [UserDao] to use for reading and writing data to the local database.
 */
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val database: UserDatabase,
    private val apiInterface: ApiInterface,
    private val userDao: UserDao
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                else -> state.lastItemOrNull()?.page ?: 0
            }
            val response = apiInterface.getUsers(loadKey + 1, state.config.pageSize)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) { userDao.deleteAll() }
                userDao.insertAll(
                    response.results.map {
                        UserEntity(it.name.first, it.name.last, it.email, response.info.page)
                    }
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
