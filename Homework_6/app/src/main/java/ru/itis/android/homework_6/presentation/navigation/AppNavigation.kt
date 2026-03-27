package ru.itis.android.homework_6.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.itis.android.homework_6.presentation.details.DetailsScreen
import ru.itis.android.homework_6.presentation.search.SearchScreen

object Screen {
    const val SEARCH = "search"
    const val DETAILS = "details/{characterId}"
    const val CHARACTER_ID_ARG = "characterId"

    fun details(id: Int) = "details/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SEARCH) {

        composable(Screen.SEARCH) {
            SearchScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.details(characterId))
                }
            )
        }

        composable(
            route = Screen.DETAILS,
            arguments = listOf(navArgument(Screen.CHARACTER_ID_ARG) { type = NavType.IntType})
        ) {
            DetailsScreen(
                onBackClick = {navController.popBackStack() }
            )
        }
    }
}
