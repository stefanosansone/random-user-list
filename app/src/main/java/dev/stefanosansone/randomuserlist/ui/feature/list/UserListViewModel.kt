package dev.stefanosansone.randomuserlist.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stefanosansone.randomuserlist.data.model.UserEntity
import dev.stefanosansone.randomuserlist.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    fun getUsers(): Flow<PagingData<UserEntity>> = userRepository.getUsers().cachedIn(viewModelScope)
}