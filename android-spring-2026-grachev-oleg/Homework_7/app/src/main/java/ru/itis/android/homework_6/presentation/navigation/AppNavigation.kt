package ru.itis.android.homework_6.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import ru.itis.android.homework_6.presentation.details.DetailsScreen
import ru.itis.android.homework_6.presentation.onboarding.OnboardingScreen
import ru.itis.android.homework_6.presentation.onboarding.OnboardingViewModel
import ru.itis.android.homework_6.presentation.search.SearchScreen

object Screen {
    const val SEARCH = "search"
    const val DETAILS = "details/{characterId}"
    const val CHARACTER_ID_ARG = "characterId"
    const val ONBOARDING = "onboarding"

    fun details(id: Int) = "details/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val analytics = remember { Firebase.analytics }

    val onboardingVm: OnboardingViewModel = hiltViewModel()
    val onboardingState by onboardingVm.shouldShow.collectAsState()

    // Capture the start destination ONCE, after the first non-loading emission.
    // Changing NavHost's startDestination later would recreate the graph and kill
    // any in-flight ViewModel (which is what caused the search to "hang" — the
    // network response arrived in an already-destroyed ViewModel).
    var startDestination by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(onboardingState) {
        if (startDestination == null && onboardingState != OnboardingViewModel.State.Loading) {
            startDestination = if (onboardingState == OnboardingViewModel.State.Show) {
                Screen.ONBOARDING
            } else {
                Screen.SEARCH
            }
        }
    }

    // Screen transition logging
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(backStackEntry) {
        val route = backStackEntry?.destination?.route ?: return@LaunchedEffect
        FirebaseCrashlytics.getInstance().log("screen_view: $route")
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, route)
        }
    }

    val start = startDestination ?: return

    NavHost(navController = navController, startDestination = start) {

        composable(Screen.ONBOARDING) {
            OnboardingScreen(
                onClose = {
                    onboardingVm.markShown()
                    navController.navigate(Screen.SEARCH) {
                        popUpTo(Screen.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SEARCH) {
            SearchScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.details(characterId))
                }
            )
        }

        composable(
            route = Screen.DETAILS,
            arguments = listOf(navArgument(Screen.CHARACTER_ID_ARG) { type = NavType.IntType })
        ) {
            DetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
