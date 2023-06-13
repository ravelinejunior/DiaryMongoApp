package com.raveline.diarymongoapp.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raveline.diary.auth.navigation.authenticationRoute
import com.raveline.diary.home.navigation.homeRoute
import com.raveline.diary.util.screens.Screens
import com.raveline.diary.write.navigation.writeRoute
import com.raveline.diarymongoapp.presentation.screens.login.LoginScreen
import com.raveline.diarymongoapp.presentation.screens.signup.SignUpScreen
import com.raveline.diarymongoapp.presentation.screens.splash.HomeSplashScreen

val TAG: String = NavGraph::class.java.simpleName

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
) {

    NavHost(
        startDestination = startDestination,
        navController = navController,
    ) {

        authenticationRoute(
            navigateToHome = {
                navController.navigate(Screens.HomeSplash.route)
            },
            onDataLoaded = onDataLoaded,
            onNavigateToLogin = {
                navController.navigate(Screens.Login.route)
            }
        )

        signUpRoute(
            navController = navController,
            onNavigateToLogin = {
                navController.navigate(Screens.Login.route)
            },
            onDataLoaded = onDataLoaded
        )

        loginRoute(
            onValueEmailChange = {

            },
            onValuePasswordChange = {

            },
            onClick = {},
            onNavigateToSignUp = {
                navController.navigate(Screens.SignUp.route)
            },
            onDataLoaded = onDataLoaded
        )

        homeSplashRoute(navController = navController)

        homeRoute(
            navigateToWrite = {
                navController.navigate(
                    Screens.Write.route, navOptions = NavOptions.Builder()
                        .setPopUpTo(
                            Screens.SignUp.route, inclusive = false
                        ).build()
                )
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(
                    route = Screens.Authentication.route,
                    navOptions = NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(
                            Screens.Home.route, inclusive = true
                        ).build()
                )

            },
            navigateToWriteWithArgs = { diaryId ->
                try {
                    navController.navigate(Screens.Write.passDiaryId(diaryId = diaryId))
                } catch (e: Exception) {
                    Log.e(TAG, "Navigation Error: ${e.message}")
                }
            },
            onDataLoaded = onDataLoaded,
        )

        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }

}


fun NavGraphBuilder.loginRoute(
    onValueEmailChange: (String) -> Unit,
    onValuePasswordChange: (String) -> Unit,
    onClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screens.Login.route) {

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

        LoginScreen(
            onValueEmailChange = onValueEmailChange,
            onValuePasswordChange = onValuePasswordChange,
            onClick = onClick,
            onNavigateToSignUp = onNavigateToSignUp
        )
    }
}

fun NavGraphBuilder.signUpRoute(
    navController: NavHostController,
    onNavigateToLogin: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screens.SignUp.route) {

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

        SignUpScreen(
            navController = navController,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

fun NavGraphBuilder.homeSplashRoute(navController: NavHostController) {
    composable(route = Screens.HomeSplash.route) {
        HomeSplashScreen(navController = navController)
    }
}

