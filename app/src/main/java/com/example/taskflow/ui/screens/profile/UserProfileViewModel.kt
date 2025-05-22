import androidx.lifecycle.ViewModel
import com.example.taskflow.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserProfileViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _profile = MutableStateFlow(UserProfile("", false))
    val profile: StateFlow<UserProfile> = _profile

    fun loadUserProfile() {
        val email = firebaseAuth.currentUser?.email ?: "Белгісіз"
        _profile.value = UserProfile(name = email.substringBefore("@"), isDarkMode = false)
    }

}