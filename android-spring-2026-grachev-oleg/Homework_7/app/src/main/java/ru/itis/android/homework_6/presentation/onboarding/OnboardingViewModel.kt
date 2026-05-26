package ru.itis.android.homework_6.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.itis.android.homework_6.data.onboarding.OnboardingRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository
) : ViewModel() {

    enum class State { Loading, Show, Hide }

    val shouldShow: StateFlow<State> = repository.isShown
        .map { if (it) State.Hide else State.Show }
        .stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading)

    fun logShown() {
        Firebase.analytics.logEvent("onboarding_shown") {}
    }

    fun markShown() {
        Firebase.analytics.logEvent("onboarding_closed") {}
        viewModelScope.launch { repository.markShown() }
    }
}
