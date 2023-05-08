package com.raveline.diarymongoapp.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.common.utlis.Constants.WRITE_SCREEN_ARGUMENT_ID
import com.raveline.diarymongoapp.data.model.MongoDB
import com.raveline.diarymongoapp.data.model.Mood
import com.raveline.diarymongoapp.data.stateModel.RequestState
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.presentation.components.DisplayAlertDialog
import com.raveline.diarymongoapp.presentation.screens.authentication.AuthenticationScreen
import com.raveline.diarymongoapp.presentation.screens.home.HomeScreen
import com.raveline.diarymongoapp.presentation.screens.login.LoginScreen
import com.raveline.diarymongoapp.presentation.screens.signup.SignUpScreen
import com.raveline.diarymongoapp.presentation.screens.splash.HomeSplashScreen
import com.raveline.diarymongoapp.presentation.screens.write.WriteScreen
import com.raveline.diarymongoapp.presentation.viewmodel.AuthenticationViewModel
import com.raveline.diarymongoapp.presentation.viewmodel.HomeViewModel
import com.raveline.diarymongoapp.presentation.viewmodel.WriteViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val TAG: String = NavGraph::class.java.simpleName

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
                navController.navigate(Screens.Write.route)
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
    onNavigateToLogin: () -> Unit,
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
            onSuccessfulFirebaseSignIn = { tokenId ->

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
            onFailureFirebaseSignIn = {
                messageBarState.addError(Exception(it))
                authViewModel.setLoading(loading = false)
            },
            onDialogDismiss = { message ->
                messageBarState.addError(Exception(message))
            },

            navigateToHome = navigateToHome,
            onNavigateToLogin = onNavigateToLogin
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

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
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
            navigateToWriteWithArgs = navigateToWriteWithArgs
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
                    FirebaseAuth.getInstance().signOut()
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
        arguments = listOf(
            navArgument(name = WRITE_SCREEN_ARGUMENT_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
    ) {

        val context = LocalContext.current
        val writeViewModel: WriteViewModel = hiltViewModel()
        val uiState = writeViewModel.uiState
        val pagerState = rememberPagerState()
        val pageNumber by remember {
            derivedStateOf { pagerState.currentPage }
        }
        val galleryState = writeViewModel.galleryState

        LaunchedEffect(key1 = uiState) {
            Log.d(TAG, "Selected Diary Id: ${uiState.selectedDiaryId}")
        }

        WriteScreen(
            uiState = uiState,
            pagerState = pagerState,
            galleryState = galleryState,
            moodName = {
                Mood.values()[pageNumber].name
            },
            onTitleChanged = {
                writeViewModel.setTitle(title = it)
            },
            onDescriptionChanged = {
                writeViewModel.setDescription(description = it)
            },
            onDeleteClicked = {
                writeViewModel.deleteDiary(
                    onSuccess = {

                        onBackPressed()

                        Toast.makeText(
                            context,
                            "${uiState.title} deleted.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                )
            },
            onBackPressed = onBackPressed,
            onSaveClicked = { diary ->
                diary?.apply {
                    //Setting the selected mood saved on viewModel
                    mood = Mood.values()[pageNumber].name
                }?.let { finalDiary ->
                    writeViewModel.upsertDiary(
                        diaryModel = finalDiary,
                        onSuccess = {
                            onBackPressed()
                        },
                        onError = {
                            Log.e(TAG, "writeRoute: $it")
                        }
                    )
                }
            },
            onDateTimeUpdated = {
                writeViewModel.updateDateTime(zonedDateTime = it)
            },
            onImageSelect = { imageUri ->

                val type = context.contentResolver.getType(imageUri)?.split("/")?.last() ?: "jpeg"

                Log.i(TAG, "OnImageSelect Called - Uri: $imageUri")

                writeViewModel.addImage(imageUri, type)

            },
            onImageDeleteClick = {
                galleryState.removeImage(it)
            }
        )
    }
}

