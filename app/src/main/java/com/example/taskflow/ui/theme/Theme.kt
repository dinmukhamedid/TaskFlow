import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun TaskFlowTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) {
        darkColorScheme(
        )
    } else {
        lightColorScheme(
        )
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
