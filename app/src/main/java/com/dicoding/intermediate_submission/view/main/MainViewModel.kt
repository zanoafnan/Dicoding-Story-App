import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate_submission.data.UserRepository
import com.dicoding.intermediate_submission.data.pref.UserModel
import com.dicoding.intermediate_submission.di.RegisterResponse
import com.dicoding.intermediate_submission.view.story.StoryActivity
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.logout()
        }
    }


    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return repository.registerUser(name, email, password)
    }
}
