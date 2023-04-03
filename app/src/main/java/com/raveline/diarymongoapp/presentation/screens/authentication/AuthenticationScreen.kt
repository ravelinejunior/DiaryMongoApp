package com.raveline.diarymongoapp.presentation.screens.authentication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.raveline.diarymongoapp.common.utlis.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismiss: (String) -> Unit,
    navigateToHome: () -> Unit
) {

    Scaffold(content = {
        ContentWithMessageBar(messageBarState = messageBarState, errorMaxLines = 3) {
            AuthenticationContent(
                loadingState = loadingState,
                onButtonClicked = onButtonClicked
            )
        }
    })

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.MONGO_SERVER_CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenIdReceived(tokenId)
            messageBarState.addSuccess("Successfully signed in!")
            Log.i("AuthenticationScreen", "TokenId = $tokenId")
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