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
import dev.stefanosansone.randomuserlist.data.network.response.UserResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalPagingApi
class UserRemoteMediatorTest {

    private lateinit var database: UserDatabase
    private lateinit var apiInterface: ApiInterface
    private lateinit var userDao: UserDao
    private lateinit var mediator: UserRemoteMediator

    @Before
    fun setup() {
        database = mockk()
        apiInterface = mockk()
        userDao = mockk()
        mediator = UserRemoteMediator(database, apiInterface, userDao)
        MockKAnnotations.init(this)
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
    }

    @Test
    fun `load data with append`() = runBlocking {
        // Set the load type of the mediator to APPEND and add mock users
        val loadType = LoadType.APPEND
        val state = mockk<PagingState<Int, UserEntity>>(relaxed = true)
        val response = UserResponse(
            info = UserResponse.Info(2),
            results = listOf(
                UserResponse.Result(
                    "joseph.hansen@example.com",
                    UserResponse.Result.Name("Joseph", "Hansen")
                ),
                UserResponse.Result(
                    "charlotte.ryan@example.com",
                    UserResponse.Result.Name("Charlotte", "Ryan")
                )
            )
        )

        every { state.lastItemOrNull() } returns UserEntity(
            "Charlotte",
            "Ryan",
            "charlotte.ryan@example.com",
            1
        )

        coEvery { apiInterface.getUsers(2, state.config.pageSize) } returns response
        coEvery { userDao.deleteAll() } just runs
        coEvery { userDao.insertAll(any()) } just runs
        coEvery { database.withTransaction(any<suspend () -> Unit>()) } coAnswers {}

        val result = mediator.load(loadType, state)


        coVerify(exactly = 0) { userDao.deleteAll() }
        coVerify(exactly = 1) { database.withTransaction(any<suspend () -> Unit>()) }

        // Check if the mediator result is in success after calling the load function
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }
}