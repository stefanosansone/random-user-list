package dev.stefanosansone.randomuserlist.data.network

import dev.stefanosansone.randomuserlist.data.network.response.UserResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiInterfaceTest {

    private val apiInterface = mockk<ApiInterface>()

    @Test
    fun `get users success`() = runBlocking {
        // Define the response to return from the mock
        val expectedResponse = UserResponse(
            info = UserResponse.Info(1),
            results = listOf(
                UserResponse.Result(
                    name = UserResponse.Result.Name("Jessie", "Vargas"),
                    email = "jessie.vargas@example.com"
                )
            )
        )
        every { runBlocking { apiInterface.getUsers(1,10) } } returns expectedResponse

        // Call the method being tested
        val result = runBlocking { apiInterface.getUsers(1,10) }

        // Verify the response
        assertEquals(expectedResponse, result)
    }

    @Test(expected = Exception::class)
    fun `get users error`() {
        // Define the exception to throw from the mock
        val expectedException = Exception("Unable to get users")
        every { runBlocking { apiInterface.getUsers(1,10) } } throws expectedException

        // Call the method being tested
        runBlocking { apiInterface.getUsers(1,10) }
    }
}
