package com.raveline.diary.home.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.diary.data.repository.MongoDB
import com.diary.data.stateModel.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.raveline.diary.home.HomeScreen
import com.raveline.diary.home.HomeViewModel
import com.raveline.diary.ui.components.DisplayAlertDialog
import com.raveline.diary.util.Constants
import com.raveline.diary.util.screens.Screens
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screens.Home.route) {

        val homeViewModel: HomeViewModel = hiltViewModel()
        val diaries by homeViewModel.diaries

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened: Boolean by remember {
            mutableStateOf(false)
        }

        var deleteAllDiariesDialogOpened by remember {
            mutableStateOf(false)
        }

        val scope = rememberCoroutineScope()

        val context = LocalContext.current

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
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            onDeleteAllDiaries = {
                deleteAllDiariesDialogOpened = true
            },
            dateIsSelected = homeViewModel.dateIsSelected,
            onDateSelected = {
                homeViewModel.getDiaries(zonedDateTime = it)
            },
            onDateReset = {
                homeViewModel.getDiaries()
            },

            )

        // Launching and initializing mongo db sync
        LaunchedEffect(key1 = Unit) {
            MongoDB.configureRealmDatabase()
        }

        // Sign out dialog
        DisplayAlertDialog(
            title = stringResource(id = com.raveline.diary.ui.R.string.sign_out_str),
            message = stringResource(id = com.raveline.diary.ui.R.string.sign_out_alert_message),
            dialogOpened = signOutDialogOpened,
            onDialogClosed = {
                signOutDialogOpened = false
            },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    FirebaseAuth.getInstance().signOut()
                    App.create(Constants.MONGO_API_KEY).currentUser?.logOut()
                    withContext(Dispatchers.Main) {
                        navigateToAuth()
                    }
                }
            }
        )

        // Delete All Diaries
        DisplayAlertDialog(
            title = stringResource(id = com.raveline.diary.ui.R.string.delete_all_diaries),
            message = stringResource(id = com.raveline.diary.ui.R.string.delete_all_diaries_message),
            dialogOpened = deleteAllDiariesDialogOpened,
            onDialogClosed = {
                deleteAllDiariesDialogOpened = false
            },
            onYesClicked = {
                homeViewModel.deleteAllDiaries(
                    onSuccess = {
                        Toast.makeText(context, "All Diaries Deleted", Toast.LENGTH_SHORT).show()
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onFailure = {
                        Toast.makeText(
                            context,
                            "Something went wrong. ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        )
    }
}