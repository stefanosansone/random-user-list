package dev.stefanosansone.randomuserlist.data.network

import dev.stefanosansone.randomuserlist.data.network.response.UserResponse
import dev.stefanosansone.randomuserlist.utils.API_SEED
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

/**
 * An interface representing the API endpoint for fetching user data.
 */
@Singleton
interface ApiInterface {
    @GET("api/?seed=${API_SEED}&inc=name,email")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") pageSize: Int
    ): UserResponse
}