package dev.stefanosansone.randomuserlist.ui.feature.list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dev.stefanosansone.randomuserlist.data.model.UserEntity
import dev.stefanosansone.randomuserlist.ui.theme.RandomUserListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val users = viewModel.getUsers().collectAsLazyPagingItems()

    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(Modifier.testTag("UserListScreen")) {
                items(users) { user ->
                    user?.let { UserListItem(it) }
                }
                item { LoadStateHandler(users.loadState.refresh) } // First load
                item { LoadStateHandler(users.loadState.append) } // Pagination
            }
        }
    }
}

@Composable
private fun UserListItem(
    user: UserEntity
) {
    Column(modifier = Modifier.padding(10.dp)) {
        with(user) {
            Text(text = "$firstName $lastName")
            Text(text = email)
        }
    }
    Divider()
}

@Composable
private fun LoadStateHandler(loadState: LoadState) {
    when (loadState) {
        is LoadState.Error -> {
            ErrorItem(loadState)
        }
        is LoadState.Loading -> {
            LoadingItem()
        }
        else -> {}
    }
}

@Composable
private fun LoadingItem() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(10.dp))
    }
}

@Composable
private fun ErrorItem(
    loadState: LoadState
) {
    (loadState as LoadState.Error).error.localizedMessage?.let {
        Text(text = "Error : $it", modifier = Modifier.padding(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun UserListItemPreview() {
    RandomUserListTheme {
        UserListItem(
            UserEntity("Stefano", "Sansone", "stefanosansone.dev@gmail.com", 1)
        )
    }
}

@Preview(name = "Light theme", showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark theme", showBackground = true)
annotation class LightAndDarkThemePreview