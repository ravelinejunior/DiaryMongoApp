package com.raveline.diary.auth.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raveline.diary.auth.AuthenticationScreen
import com.raveline.diary.auth.AuthenticationViewModel
import com.raveline.diary.util.screens.Screens
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState


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
