import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserProfileScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: UserProfileViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Аты: ${profile.name}", style = MaterialTheme.typography.headlineMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Түнгі режим:")
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleTheme() }
            )
        }
    }
}