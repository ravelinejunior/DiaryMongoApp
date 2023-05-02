package com.raveline.diarymongoapp.presentation.screens.authentication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.raveline.diarymongoapp.common.utlis.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    onSuccessfulFirebaseSignIn: (String) -> Unit,
    onFailureFirebaseSignIn: (Exception) -> Unit,
    onDialogDismiss: (String) -> Unit,
    navigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(), content = {
            ContentWithMessageBar(messageBarState = messageBarState, errorMaxLines = 3) {
                AuthenticationContent(
                    loadingState = loadingState,
                    onButtonClicked = onButtonClicked,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        })

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.MONGO_SERVER_CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            val credential = GoogleAuthProvider.getCredential(tokenId, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccessfulFirebaseSignIn(tokenId)
                    } else {
                        task.exception?.let {
                            onFailureFirebaseSignIn(it)
                        }
                    }
                }
        },
        onDialogDismissed = { message ->
            onDialogDismiss(message)
            messageBarState.addError(Exception(message))
            Log.e("AuthenticationScreen", "Message = $message")
        }
    )

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            // Navigate to home screen
            navigateToHome()
        }
    }

}