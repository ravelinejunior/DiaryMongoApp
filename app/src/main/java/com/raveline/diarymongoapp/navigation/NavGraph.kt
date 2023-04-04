package com.raveline.diarymongoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.presentation.screens.authentication.AuthenticationScreen
import com.raveline.diarymongoapp.presentation.screens.splash.HomeSplashScreen
import com.raveline.diarymongoapp.presentation.viewmodel.AuthenticationViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController) {

    NavHost(
        startDestination = startDestination,
        navController = navController,
    ) {
        authenticationRoute(
            navigateToHome = {
                navController.navigate(Screens.HomeSplash.route)
            }
        )
        homeSplashRoute(navController = navController)
        homeRoute()
        writeRoute()
    }

}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
) {
    composable(route = Screens.Authentication.route) {

        val authViewModel = viewModel<AuthenticationViewModel>()
        val loadingState by authViewModel.loadingState
        val authenticated by authViewModel.authenticated

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            onButtonClicked = {
                oneTapState.open()
                authViewModel.setLoading(loading = true)
            },
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState,
            onTokenIdReceived = { tokenId ->

                //Authenticating user and verify if its all good.
                authViewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated!")
                        authViewModel.setLoading(loading = false)

                    },
                    onError = { error ->
                        messageBarState.addError(Exception(error))
                        authViewModel.setLoading(loading = false)
                    }
                )
            },
            onDialogDismiss = { message ->
                messageBarState.addError(Exception(message))
            },
            navigateToHome = navigateToHome,
        )
    }
}

fun NavGraphBuilder.homeSplashRoute(navController: NavHostController) {
    composable(route = Screens.HomeSplash.route) {
        HomeSplashScreen(navController = navController)
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