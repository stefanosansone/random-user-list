package dev.stefanosansone.randomuserlist.data.network.response

data class UserResponse(
    val info: Info,
    val results: List<Result>
){
    data class Info(
        val page: Int
    )
    data class Result(
        val email: String,
        val name: Name
    ){
        data class Name(
            val first: String,
            val last: String
        )
    }
}