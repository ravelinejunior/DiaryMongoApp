package com.raveline.diarymongoapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.common.utlis.RequestState
import com.raveline.diarymongoapp.data.model.MongoDB
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.presentation.components.DisplayAlertDialog
import com.raveline.diarymongoapp.presentation.screens.authentication.AuthenticationScreen
import com.raveline.diarymongoapp.presentation.screens.home.HomeScreen
import com.raveline.diarymongoapp.presentation.screens.splash.HomeSplashScreen
import com.raveline.diarymongoapp.presentation.screens.write.WriteScreen
import com.raveline.diarymongoapp.presentation.viewmodel.AuthenticationViewModel
import com.raveline.diarymongoapp.presentation.viewmodel.HomeViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            onDataLoaded = onDataLoaded
        )
        homeSplashRoute(navController = navController)
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screens.Write.route)
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screens.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screens.Authentication.route) {

        val authViewModel = viewModel<AuthenticationViewModel>()
        val loadingState by authViewModel.loadingState
        val authenticated by authViewModel.authenticated

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

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

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screens.Home.route) {

        val homeViewModel: HomeViewModel = viewModel()
        val diaries by homeViewModel.diaries

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened: Boolean by remember {
            mutableStateOf(false)
        }

        val scope = rememberCoroutineScope()

        // dismiss splash screen
        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = {
                signOutDialogOpened = true
            },
            navigateToWrite = navigateToWrite,
        )

        // Launching and initializing mongo db sync
        LaunchedEffect(key1 = Unit) {
            MongoDB.configureRealmDatabase()
        }

        // Sign out dialog
        DisplayAlertDialog(
            title = stringResource(id = R.string.sign_out_str),
            message = stringResource(id = R.string.sign_out_alert_message),
            dialogOpened = signOutDialogOpened,
            onDialogClosed = {
                signOutDialogOpened = false
            },
            onYesClicked = {
                scope.launch(IO) {
                    App.create(Constants.MONGO_API_KEY).currentUser?.logOut()
                    withContext(Main) {
                        navigateToAuth()
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit
) {

    composable(
        route = Screens.Write.route,
        arguments = listOf(navArgument(name = Constants.WRITE_SCREEN_ARGUMENT_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

        val pagerState = rememberPagerState()

        WriteScreen(
            selectedDiary = null,
            pagerState = pagerState,
            onDeleteClicked = { },
            onBackPressed = onBackPressed
        )
    }
}