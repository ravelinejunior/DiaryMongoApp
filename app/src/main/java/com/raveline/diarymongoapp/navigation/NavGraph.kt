package com.raveline.diarymongoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.presentation.screens.authentication.AuthenticationScreen
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController) {

    NavHost(
        startDestination = startDestination,
        navController = navController,
    ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }

}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screens.Authentication.route) {

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            loadingState = oneTapState.opened,
            onButtonClicked = {
                oneTapState.open()
            },
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screens.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screens.Write.route,
        arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARGUMENT_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}